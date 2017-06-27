package com.sheaconlon.realcraft;

/**
 * A worker.
 */
public abstract class Worker implements Runnable {
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
        while (!Thread.interrupted()) {
            this.tick();
        }
    }
}
