package com.sheaconlon.realcraft.utilities;

/**
 * A node in a doubly linked list.
 * @param <T> The type of the contents of the node.
 */
public class LinkNode<T> {
    private T contents;
    private LinkNode<T> prev;
    private LinkNode<T> next;

    /**
     * Create a node.
     * @param contents The contents of the node.
     */
    public LinkNode(final T contents) {
        this.contents = contents;
        this.prev = null;
        this.next = null;
    }

    /**
     * Get the contents of this node.
     * @return The contents of this node.
     */
    public T contents() {
        return this.contents;
    }

    /**
     * Set the contents of this node.
     * @param contents The new contents.
     */
    public void setContents(final T contents) {
        this.contents = contents;
    }

    /**
     * Get the node after this node in the linked list.
     * @return The node after this node in the linked list. May be null.
     */
    public LinkNode<T> next() {
        return this.next;
    }

    /**
     * Insert this node into a linked list, after some other node.
     * @param node The node after which this node should be inserted.
     */
    public void putAfter(final LinkNode<T> node) {
        if (node.next == null) {
            throw new RuntimeException("cannot put after a node that has node.next = null");
        }
        this.prev = node;
        this.next = node.next;
        this.prev.next = this;
        this.next.prev = this;
    }

    /**
     * Insert this node into a linked list, before some other node.
     * @param node The node before which this node should be inserted.
     */
    public void putBefore(final LinkNode<T> node) {
        if (node.prev == null) {
            throw new RuntimeException("cannot put before a node that has node.prev = null");
        }
        this.next = node;
        this.prev = node.prev;
        this.prev.next = this;
        this.next.prev = this;
    }

    /**
     * Remove this node from the linked list it is in, if any.
     */
    public void remove() {
        if (this.prev != null) {
            this.prev.next = this.next;
        }
        if (this.next != null) {
            this.next.prev = this.prev;
        }
    }
}
