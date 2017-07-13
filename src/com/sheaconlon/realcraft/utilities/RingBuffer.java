package com.sheaconlon.realcraft.utilities;

/**
 * A simple fixed-capacity ring buffer.
 */
public class RingBuffer<E> {
    private final E[] contents;

    private int curr;

    private int size;

    /**
     * Create a ring buffer.
     * @param cap The desired capacity for the ring buffer.
     */
    public RingBuffer(final int cap) {
        this.contents = (E[])(new Object[cap]);
        this.curr = 0;
        this.size = 0;
    }

    /**
     * @return The number of elements in this ring buffer.
     */
    public int size() {
        return this.size;
    }

    /**
     * Add an element, replacing the oldest existing element if needed.
     * @param e The element.
     * @return The element that was replaced, or null if there is none.
     */
    public E addFront(E e) {
        final E old = this.contents[this.curr];
        this.size = Math.min(this.size + 1, this.contents.length);
        this.contents[this.curr] = e;
        this.curr = (this.curr + 1) % this.contents.length;
        return old;
    }

    /**
     * @return The last element in this ring buffer, or null if there is none.
     */
    public E getLast() {
        if (this.size < 1) {
            return null;
        }
        return this.contents[MathUtilities.modulo(this.curr - 1, this.contents.length)];
    }
}
