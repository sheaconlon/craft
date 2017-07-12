package com.sheaconlon.realcraft.concurrency;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A runner of tasks.
 */
public class TaskRunner implements Runnable {
    private static final Queue<Task> queue = new PriorityBlockingQueue<>();

    /**
     * Run tasks, high-priority ones first.
     */
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            final Worker topTask = queue.poll();
            topTask.run();
            if (!topTask.done()) {
                TaskRunner.enqueueTask(topTask);
            }
        }
    }

    /**
     * Enqueue a task for running by any task runner.
     * @param task The task.
     */
    public static void enqueueTask(final Task task) {
        TaskRunner.queue.add(task);
    }
}
