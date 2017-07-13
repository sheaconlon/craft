package com.sheaconlon.realcraft.utilities;

/**
 * Something which takes a rolling average.
 */
public class RollingAverager {
    private final int samples;

    private RingBuffer<Double> buffer;

    private double sum;

    /**
     * Create a rolling averager.
     * @param samples The number of samples that it should average over.
     */
    public RollingAverager(final int samples) {
        this.samples = samples;
        this.buffer = new RingBuffer<>(this.samples);
        this.sum = 0;
    }

    /**
     * Add a sample.
     * @param x The sample.
     */
    public void add(final double x) {
        final Double old = this.buffer.addFront(x);
        if (old != null) {
            this.sum -= old;
        }
        this.sum += x;
    }

    /**
     * @return The rolling average of the samples.
     */
    public double average() {
        return this.sum / this.buffer.size();
    }
}
