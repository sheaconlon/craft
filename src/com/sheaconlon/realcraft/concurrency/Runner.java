package com.sheaconlon.realcraft.concurrency;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * A runner, which ticks workers.
 */
public class Runner implements Runnable {
    private final int NO_WORKER_SLEEP_TIME = 10;

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
        for (final Worker w : this.assignedWorkers) {
            w.initInThread();
        }
        while (!Thread.interrupted()) {
            Worker topSharedWorker = this.sharedWorkers.poll();
            if (topSharedWorker != null && topSharedWorker.timeUntilTickDue() > -1) {
                this.sharedWorkers.add(topSharedWorker);
                topSharedWorker = null;
            }
            Worker topAssignedWorker = this.assignedWorkers.poll();
            if (topAssignedWorker != null && topAssignedWorker.timeUntilTickDue() > -1) {
                this.assignedWorkers.add(topAssignedWorker);
                topAssignedWorker = null;
            }
            if (topSharedWorker == null && topAssignedWorker == null) {
                try {
                    Thread.sleep(NO_WORKER_SLEEP_TIME);
                } catch (final InterruptedException e) {
                    return;
                }
                continue;
            }
            if (topSharedWorker == null) {
                topAssignedWorker.tick();
                this.assignedWorkers.add(topAssignedWorker);
                continue;
            }
            if (topAssignedWorker == null) {
                topSharedWorker.tick();
                this.sharedWorkers.add(topSharedWorker);
                continue;
            }
            if (topSharedWorker.compareTo(topAssignedWorker) < 0) { // Ties go to assigned workers.
                this.assignedWorkers.add(topAssignedWorker);
                topSharedWorker.tick();
                this.sharedWorkers.add(topSharedWorker);
            } else {
                this.sharedWorkers.add(topSharedWorker);
                topAssignedWorker.tick();
                this.assignedWorkers.add(topAssignedWorker);
            }
        }
    }

    /**
     * Assign a worker for this runner to tick. This worker is not ticked by other runners. Should not be called
     * once {@link #run()} is called.
     * @param w The worker.
     */
    public void assign(final Worker w) {
        this.assignedWorkers.add(w);
    }
}
