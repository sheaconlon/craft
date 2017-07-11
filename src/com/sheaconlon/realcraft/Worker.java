package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.utilities.ArrayUtilities;

/**
 * A worker.
 */
public abstract class Worker implements Runnable {
    /**
     * The number of ticks that should be considered "beginning" ticks.
     */
    protected static final int BEGINNING_TICKS = 1000;

    /**
     * The number of recent ticks that a worker should consider for short-term statistics.
     */
    private static final int SHORT_TERM_TICKS = 10;

    /**
     * The number of recent ticks that a worker should consider for long-term statistics.
     */
    private static final int LONG_TERM_TICKS = Worker.SHORT_TERM_TICKS * 100;

    /**
     * The number of milliseconds in a second.
     */
    private static final int MILLISECONDS_PER_SECOND = 1_000;

    /**
     * The number of nanoseconds in a second.
     */
    private static final int NANOSECONDS_PER_SECOND = 1_000_000_000;

    /**
     * The number of nanoseconds in a millisecond.
     */
    private static final int NANOSECONDS_PER_MILLISECOND = Worker.NANOSECONDS_PER_SECOND
            / Worker.MILLISECONDS_PER_SECOND;

    /**
     * The time that this worker's {@link #tick(double)} method was last called, in nanoseconds since some origin.
     */
    private long lastTickTime;

    /**
     * The minimum interval that should pass between calls of {@link #tick(double)}, in nanoseconds.
     */
    private long minInterval;

    /**
     * The actual intervals that have passed before calls of {@link #tick(double)}, for the last {@link #SHORT_TERM_TICKS}
     * calls.
     */
    private long[] shortTermIntervals;

    /**
     * The index into {@link #shortTermIntervals} into which an interval should next be placed.
     */
    private int currShortTermIndex;

    /**
     * The actual intervals that have passed before calls of {@link #tick(double)}, for the last {@link #LONG_TERM_TICKS}
     * calls.
     */
    private long[] longTermIntervals;

    /**
     * The index into {@link #longTermIntervals} into which an interval should next be placed.
     */
    private int currLongTermIndex;

    /**
     * The number of times this worker's {@link #tick(double)} method has been called.
     */
    protected long ticks;

    /**
     * Create a worker.
     *
     * The worker will pretend that its {@link #tick(double)} method was called when it was created, though it actually was
     * not.
     */
    protected Worker() {
        this.lastTickTime = System.nanoTime();
        this.minInterval = this.getInitialMinInterval();
        this.shortTermIntervals = new long[Worker.SHORT_TERM_TICKS];
        this.currShortTermIndex = 0;
        this.longTermIntervals = new long[Worker.LONG_TERM_TICKS];
        this.currLongTermIndex = 0;
        this.ticks = 0;
    }

    /**
     * Get an initial value for {@link #minInterval}.
     * @return An initial value for {@link #minInterval}.
     */
    protected abstract long getInitialMinInterval();

    /**
     * Get the value of {@link #minInterval}.
     * @return The value of {@link #minInterval}.
     */
    public long getMinInterval() {
        if (this.ticks < Worker.BEGINNING_TICKS) {
            return 0;
        }
        if (this.ticks == Worker.BEGINNING_TICKS) {
            System.out.println(this.getClass().getSimpleName());
        }
        return this.minInterval;
    }

    /**
     * Make this worker do less ticks per unit time.
     */
    void lessTicks() {
        this.minInterval *= 2;
    }

    /**
     * Make this worker do more ticks per unit time.
     */
    void moreTicks() {
        this.minInterval /= 2;
    }

    /**
     * Do some small amount of work.
     * @param elapsedTime The amount of wall time that has passed since the last call to this method, in seconds.
     */
    protected abstract void tick(final double elapsedTime);

    /**
     * Do initialization work in the thread this worker runs in.
     */
    protected abstract void initInThread();

    /**
     * Return whether this worker should stop running.
     * @return Whether this worker should stop running.
     */
    protected boolean shouldStop() {
        return Thread.interrupted();
    }

    /**
     * Run this worker.
     *
     * Do initialization work, then repeatedly call {@link #tick(double)} with no less than {@link #getMinInterval()}}
     * nanoseconds between each call. Return when {@link #shouldStop()} returns true.
     */
    public void run() {
        this.initInThread();
        while (!this.shouldStop()) {
            final long interval = System.nanoTime() - this.lastTickTime;
            final long intervalRemaining = this.getMinInterval() - interval;
            if (intervalRemaining > 0) {
                final int milliseconds = (int)(intervalRemaining / Worker.NANOSECONDS_PER_MILLISECOND);
                final int nanoseconds = (int)(intervalRemaining % Worker.NANOSECONDS_PER_MILLISECOND);
                try {
                    Thread.sleep(milliseconds, nanoseconds);
                } catch (final InterruptedException e) {
                    return;
                }
            }
            final double elapsedTime = (double)this.recordInterval() / Worker.NANOSECONDS_PER_SECOND;
            this.tick(elapsedTime);
        }
    }

    /**
     * Record the interval that passed between the last call to {@link #tick(double)} and one occurring right now.
     * @return The length of the interval, in nanoseconds.
     */
    private long recordInterval() {
        final long currentTime = System.nanoTime();
        final long interval = currentTime - this.lastTickTime;
        this.lastTickTime = currentTime;
        this.shortTermIntervals[this.currShortTermIndex] = interval;
        this.currShortTermIndex = (this.currShortTermIndex + 1) % this.shortTermIntervals.length;
        this.longTermIntervals[this.currLongTermIndex] = interval;
        this.currLongTermIndex = (this.currLongTermIndex + 1) % this.longTermIntervals.length;
        this.ticks++;
        return interval;
    }

    /**
     * Get the maximum of the intervals that have passed before calls of {@link #tick()}, for the last
     * {@link #SHORT_TERM_TICKS} calls, or -1 if there is not yet enough data.
     * @return The maximum of the intervals that have passed before calls of {@link #tick()}, for the last
     * {@link #SHORT_TERM_TICKS} calls, or -1 if there is not yet enough data.
     */
    private long getShortTermMaxInterval() {
        if (this.ticks < Worker.SHORT_TERM_TICKS) {
            return -1;
        }
        return ArrayUtilities.max(this.shortTermIntervals);
    }

    /**
     * Get the maximum of the intervals that have passed before calls of {@link #tick()}, for the last
     * {@link #LONG_TERM_TICKS} calls, or -1 if there is not yet enough data.
     * @return The maximum of the intervals that have passed before calls of {@link #tick()}, for the last
     * {@link #LONG_TERM_TICKS} calls, or -1 if there is not yet enough data.
     */
    private long getLongTermMaxInterval() {
        if (this.ticks < Worker.LONG_TERM_TICKS) {
            return -1;
        }
        return ArrayUtilities.max(this.longTermIntervals);
    }
}
