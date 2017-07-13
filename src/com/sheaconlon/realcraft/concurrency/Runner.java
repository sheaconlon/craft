package com.sheaconlon.realcraft.concurrency;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A runner, which ticks workers.
 */
public class Runner implements Runnable {
    private final Queue<Worker> sharedWorkers;

    private final Queue<Worker> assignedWorkers;

    /**
     * Create a runner.
     * @param sharedWorkers Workers which this runner should tick. This queue may be shared with other runners.
     */
    public Runner(final Queue<Worker> sharedWorkers) {
        this.sharedWorkers = sharedWorkers;
        this.assignedWorkers = new PriorityQueue<>();
    }

    /**
     * Tick workers, preferring ones that are "small" according to their natural ordering.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            final Worker topSharedWorker = this.sharedWorkers.poll();
            final Worker topAssignedWorker = this.assignedWorkers.poll();
            if (topSharedWorker.compareTo(topAssignedWorker) < 0) { // Ties go to assigned workers.
                topSharedWorker.tick();
                this.sharedWorkers.add(topSharedWorker);
            } else {
                topAssignedWorker.tick();
                this.assignedWorkers.add(topAssignedWorker);
            }
        }
    }

    /**
     * Assign a worker for this runner to tick. This worker is not ticked by other runners.
     * @param w The worker.
     */
    public void assign(final Worker w) {
        this.assignedWorkers.add(w);
    }
}
