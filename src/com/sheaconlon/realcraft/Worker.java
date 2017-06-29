package com.sheaconlon.realcraft;

/**
 * A worker.
 */
public abstract class Worker implements Runnable {
    /**
     * The number of nanoseconds in a second.
     */
    private static final int NANOSECONDS_PER_SECOND = 1_000_000_000;

    /**
     * The number of nanoseconds in a millisecond
     */
    private static final int NANOSECONDS_PER_MILLISECOND = 1_000_000;

    /**
     * The time of the last tick of this worker, in nanoseconds since some fixed, arbitrary point.
     */
    private long lastTickTime;

    protected Worker() {
        this.lastTickTime = System.nanoTime();
    }

    /**
     * Get the target tick rate of this worker, in ticks per second.
     */
    protected abstract int getTargetTickRate();

    /**
     * Do some work.
     */
    public abstract void tick();

    /**
     * Perform initialization work in the thread this worker will run in.
     */
    public abstract void inThreadInitialize();

    /**
     * Run this worker.
     */
    public void run() {
        this.inThreadInitialize();
        final double targetTickPeriod = 1 / (double)this.getTargetTickRate() * Worker.NANOSECONDS_PER_SECOND;
        while (!Thread.interrupted()) {
            final double timeRemaining = targetTickPeriod - (System.nanoTime() - this.lastTickTime);
            this.lastTickTime = System.nanoTime();
            if (timeRemaining > 0) {
                try {
                    Thread.sleep((int)(timeRemaining / Worker.NANOSECONDS_PER_MILLISECOND),
                            (int)(timeRemaining % Worker.NANOSECONDS_PER_MILLISECOND));
                } catch (final InterruptedException e) {
                    return;
                }
            }
            this.tick();
        }
    }
}
