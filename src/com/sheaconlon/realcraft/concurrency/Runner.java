package com.sheaconlon.realcraft.concurrency;

import java.util.Queue;

/**
 * A runner, which ticks workers.
 */
public class Runner implements Runnable {
    private final Queue<Worker> workerQueue;

    /**
     * Create a runner.
     * @param workerQueue The queue of workers which this runner should draw from.
     */
    public Runner(final Queue<Worker> workerQueue) {
        this.workerQueue = workerQueue;
    }

    /**
     * Tick workers, following the order of the queue.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            final Worker topTask = this.workerQueue.poll();
            topTask.tick();
            this.workerQueue.add(topTask);
        }
    }
}
