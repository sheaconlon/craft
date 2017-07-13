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

    private long lastRunTime;

    private final RollingAverager tickIntervalAverager;

    /**
     * Create a worker.
     *
     * Its {@link #tick()} method will be called once.
     */
    protected Worker() {
        this.lastRunTime = System.nanoTime();
        this.tickIntervalAverager = new RollingAverager(Worker.AVERAGE_TICK_INTERVAL_SAMPLE_SIZE);
        this.tick();
    }

    /**
     * Record the interval that has passed since the last call to {@link #tick()} and call {@link #tick()}.
     */
    void tickAndRecord() {
        this.tickIntervalAverager.add(nsToS(System.nanoTime() - this.lastRunTime));
        this.tick();
    }

    /**
     * @return The average of the last {@link #AVERAGE_TICK_INTERVAL_SAMPLE_SIZE} intervals that has passed
     * between calls to {@link #tick()}.
     */
    double averageTickInterval() {
        return this.tickIntervalAverager.average();
    }

    protected abstract void tick();

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
        final double elapsedTime = nsToS(System.nanoTime() - this.lastRunTime);
        return 1 / this.getTargetFreq() - elapsedTime;
    }

    private static double nsToS(final long ns) {
        return (double)ns / (double)NANOSECONDS_PER_SECOND;
    }
}
