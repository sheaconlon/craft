package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.utilities.ArrayUtilities;

/**
 * A worker.
 */
public abstract class Worker implements Runnable {
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
     * The time that this worker's {@link #tick()} method was last called, in nanoseconds since some origin.
     */
    private long lastTickTime;

    /**
     * The minimum interval that should pass between calls of {@link #tick()}, in nanoseconds.
     */
    private long minInterval;

    /**
     * The actual intervals that have passed before calls of {@link #tick()}, for the last {@link #SHORT_TERM_TICKS}
     * calls.
     */
    private long[] shortTermIntervals;

    /**
     * The index into {@link #shortTermIntervals} into which an interval should next be placed.
     */
    private int currShortTermIndex;

    /**
     * The actual intervals that have passed before calls of {@link #tick()}, for the last {@link #LONG_TERM_TICKS}
     * calls.
     */
    private long[] longTermIntervals;

    /**
     * The index into {@link #longTermIntervals} into which an interval should next be placed.
     */
    private int currLongTermIndex;

    /**
     * The number of times this worker's {@link #tick()} method has been called.
     */
    private long ticks;

    /**
     * Create a worker.
     *
     * The worker will pretend that its {@link #tick()} method was called when it was created, though it actually was
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
    long getMinInterval() {
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
     */
    protected abstract void tick();

    /**
     * Do initialization work in the thread this worker runs in.
     */
    protected abstract void initInThread();

    /**
     * Return whether this worker should stop running.
     * @return Whether this worker should stop running.
     */
    protected abstract boolean shouldStop();

    /**
     * Run this worker.
     *
     * Do initialization work, then repeatedly call {@link #tick()} with no less than {@link #getMinInterval()}}
     * nanoseconds between each call. Return when {@link #shouldStop()} returns true.
     */
    public void run() {
        this.initInThread();
        while (!this.shouldStop()) {
            final long interval = System.nanoTime() - this.lastTickTime;
            this.lastTickTime = System.nanoTime();
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
            this.recordInterval();
            this.tick();
        }
    }

    /**
     * Record the interval that passed between the last call to {@link #tick()} and one occurring right now.
     */
    private void recordInterval() {
        final long currentTime = System.nanoTime();
        final long interval = currentTime - this.lastTickTime;
        this.lastTickTime = currentTime;
        this.shortTermIntervals[this.currShortTermIndex] = interval;
        this.currShortTermIndex++;
        this.longTermIntervals[this.currLongTermIndex] = interval;
        this.currLongTermIndex++;
        this.ticks++;
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
