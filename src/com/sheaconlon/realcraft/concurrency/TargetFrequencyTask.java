package com.sheaconlon.realcraft.concurrency;

/**
 * A task which should be run repeatedly, with some target frequency.
 *
 * Consider a target frequency task {@code t}. Calls to {@code t.run()} should be initiated no less than
 * {@code 1 / t.getTargetFreq()} seconds apart. Ideally, they would be initiated exactly that many seconds
 * apart. Under load, they would probably be initiated more than that many seconds apart.
 */
public abstract class TargetFrequencyTask extends Task {
    private long lastRunTime;

    private static final long NANOSECONDS_PER_SECOND = 1_000_000_000;

    /**
     * Create a target frequency task.
     *
     * Its {@link #run()} method will be called once.
     */
    protected TargetFrequencyTask() {
        this.lastRunTime = System.nanoTime();
        this.run();
    }

    @Override
    public boolean done() {
        return false;
    }

    /**
     * Get the target frequency of this target frequency task.
     * @return See above. In Hertz.
     */
    protected abstract double getTargetFreq();

    /**
     * The amount of time until a call to {@link #run()} is due, in seconds.
     */
    protected double priority() {
        final double elapsedTime = TargetFrequencyTask.nsToS(System.nanoTime() - this.lastRunTime);
        return 1 / this.getTargetFreq() - elapsedTime;
    }

    private static double nsToS(final long ns) {
        return (double)ns / (double)TargetFrequencyTask.NANOSECONDS_PER_SECOND;
    }
}
