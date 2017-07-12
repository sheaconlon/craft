package com.sheaconlon.realcraft.concurrency;

import java.util.Queue;

/**
 * A runner of tasks.
 */
public class TaskRunner implements Runnable {
    private final Queue<Worker> workerQueue;

    /**
     * Create a task runner.
     * @param workerQueue The queue of workers which this task runner should draw from.
     */
    public TaskRunner(final Queue<Worker> workerQueue) {
        this.workerQueue = workerQueue;
    }

    /**
     * Run tasks, high-priority ones first.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            final Worker topTask = this.workerQueue.poll();
            topTask.run();
            if (!topTask.done()) {
                this.workerQueue.add(topTask);
            }
        }
    }
}
