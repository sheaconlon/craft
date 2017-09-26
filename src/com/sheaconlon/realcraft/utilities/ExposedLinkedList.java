package com.sheaconlon.realcraft.utilities;

/**
 * A linked list which exposes its nodes to the client.
 */
public class ExposedLinkedList<T> {
    private final DoublyLinkedNode<T> sentinel;

    /**
     * Create an exposed linked list.
     */
    public ExposedLinkedList() {
        this.sentinel = new DoublyLinkedNode<>(null);
    }

    /**
     * Add a node to the back of this list.
     * @param node The node.
     */
    public void addBack(final DoublyLinkedNode<T> node) {
        node.insertBefore(sentinel);
    }

    /**
     * Remove a node from the front of this list.
     * @return The node.
     */
    public DoublyLinkedNode<T> removeFront() {
        final DoublyLinkedNode<T> node = this.sentinel.next();
        node.remove();
        return node;
    }
}
