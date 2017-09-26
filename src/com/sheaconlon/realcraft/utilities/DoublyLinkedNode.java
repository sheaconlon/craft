package com.sheaconlon.realcraft.utilities;

/**
 * A node in a doubly linked list.
 * @param <T> The type of the elements of the list.
 */
public class DoublyLinkedNode<T> {
    private T contents;
    private DoublyLinkedNode<T> prev;
    private DoublyLinkedNode<T> next;

    /**
     * Create a doubly linked node.
     * @param contents The element to be stored in this node.
     */
    public DoublyLinkedNode(final T contents) {
        this.contents = contents;
    }

    /**
     * Get the contents of this node.
     * @return The contents of this node.
     */
    public T contents() {
        return this.contents;
    }

    /**
     * Get the node after this node in the list.
     * @return The node after this node in the list.
     */
    public DoublyLinkedNode<T> next() {
        return this.next;
    }

    /**
     * Set the contents of this node.
     * @param contents The new contents.
     */
    public void setContents(final T contents) {
        this.contents = contents;
    }

    /**
     * Insert this node into a list, after some node.
     * @param node The node after which this node should be inserted.
     */
    public void insertAfter(final DoublyLinkedNode<T> node) {
        this.prev = node;
        this.next = node.next;
        this.prev.next = this;
        this.next.prev = this;
    }

    /**
     * Insert this node into a list, before some node.
     * @param node The node before which this node should be inserted.
     */
    public void insertBefore(final DoublyLinkedNode<T> node) {
        this.insertAfter(node.prev.prev);
    }

    /**
     * Remove this node from the list it is in.
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
