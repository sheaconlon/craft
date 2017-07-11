package com.sheaconlon.realcraft.concurrency;

/**
 * A task; something which needs to be run.
 */
public abstract class Task implements Comparable<Task> {
    /**
     * Run this task.
     */
    public abstract void run();

    /**
     * "Smaller" tasks are higher priority tasks.
     */
    @Override
    public int compareTo(final Task other) {
        return -(int)Math.signum(this.priority() - other.priority());
    }

    /**
     * Should return the name of this task.
     */
    @Override
    public abstract String toString();

    /**
     * Get the priority of this task.
     *
     * Larger priorities indicate more important tasks.
     * @return See above. In units of priority.
     */
    protected abstract double priority();
}
