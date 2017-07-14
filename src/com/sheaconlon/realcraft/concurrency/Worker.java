package com.sheaconlon.realcraft.concurrency;

import com.sheaconlon.realcraft.utilities.RollingAverager;

/**
 * A worker, which should be ticked repeatedly.
 *
 * Consider a worker {@code w}. Calls to {@code w.tick()} should be initiated no less than
 * {@code 1 / w.getTargetFreq()} seconds apart. Ideally, they would be initiated exactly that many seconds
 * apart. Under load, they would probably be initiated more than that many seconds apart.
 */
public abstract class Worker implements Comparable<Worker> {
    private static final int AVERAGE_TICK_INTERVAL_SAMPLE_SIZE = 1000;

    private static final long NANOSECONDS_PER_SECOND = 1_000_000_000;

    private long lastTickTime;

    private final RollingAverager tickIntervalAverager;

    /**
     * Create a worker.
     *
     * It will consider its first tick to have occurred upon construction.
     */
    protected Worker() {
        this.lastTickTime = System.nanoTime();
        this.tickIntervalAverager = new RollingAverager(Worker.AVERAGE_TICK_INTERVAL_SAMPLE_SIZE);
        this.lastTickTime = System.nanoTime();
    }

    @Override
    public abstract String toString();

    /**
     * @return Whether this worker needs to be ticked on the main thread.
     */
    public abstract boolean needsMainThread();

    /**
     * @return Whether this worker needs to be ticked on a single, consistent thread.
     */
    public abstract boolean needsDedicatedThread();

    /**
     * Record the interval that has passed since the last call to this method and do some bit of work.
     */
    void tick() {
        final double elapsedTime = nsToS(System.nanoTime() - this.lastTickTime);
        System.out.printf("%s was ticked by thread %s after %f seconds.\n", this, Thread.currentThread().getName(), elapsedTime);
        this.lastTickTime = System.nanoTime();
        this.tickIntervalAverager.add(elapsedTime);
        this.tick(elapsedTime);
    }

    /**
     * @return The average of the last {@link #AVERAGE_TICK_INTERVAL_SAMPLE_SIZE} intervals that has passed
     * between calls to {@link #tick()}.
     */
    double averageTickInterval() {
        return this.tickIntervalAverager.average();
    }

    /**
     * Do some bit of work.
     * @param interval The interval that has elapsed since the last call to this method. In seconds.
     */
    protected abstract void tick(final double interval);

    /**
     * "Smaller" workers are higher priority workers.
     */
    @Override
    public int compareTo(final Worker other) {
        return -(int)Math.signum(this.priority() - other.priority());
    }

    /**
     * Get the target frequency of this worker.
     * @return See above. In Hertz.
     */
    protected abstract double getTargetFreq();

    /**
     * The amount of time until a call to {@link #tick()} is due, in seconds.
     */
    protected double priority() {
        final double elapsedTime = nsToS(System.nanoTime() - this.lastTickTime);
        return 1 / this.getTargetFreq() - elapsedTime;
    }

    private static double nsToS(final long ns) {
        return (double)ns / (double)NANOSECONDS_PER_SECOND;
    }
}
