package com.sheaconlon.realcraft.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * An LRU cache. Associates keys with values. Has a limited capacity. Evicts the least recently used entry.
 */
public class LRUCache<K, V> {
    private static class Entry<EK, EV> {
        private final EK key;
        private final EV value;

        Entry(final EK key, final EV value) {
            this.key = key;
            this.value = value;
        }

        EK key() {
            return this.key;
        }

        EV value() {
            return this.value;
        }
    }

    private final int capacity;
    private final Map<K, LinkNode<Entry<K, V>>> keyToNodeMap;
    private final ExposedLinkedList<Entry<K, V>> queue;

    /**
     * Create an LRU cache.
     * @param capacity The capacity of the cache.
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyToNodeMap = new HashMap<>();
        this.queue = new ExposedLinkedList<>();
    }

    /**
     * Get the value associated with some key.
     * @param key The key.
     * @return The value. Null if there no value associated with {@code key}.
     */
    public V get(final K key) {
        final LinkNode<Entry<K, V>> node = this.keyToNodeMap.get(key);
        if (node == null) {
            return null;
        }
        this.remove(node);
        this.add(node);
        return node.contents().value();
    }

    /**
     * Set the value associated with some key, adding an entry for the key if not present.
     * @param key The key.
     * @param value The value.
     */
    public void put(final K key, final V value) {
        LinkNode<Entry<K, V>> node = this.keyToNodeMap.get(key);
        if (node == null) {
            final Entry<K, V> newEntry = new Entry<>(key, value);
            node = new LinkNode<>(newEntry);
        } else {
            this.remove(node);
        }
        this.add(node);
    }

    private void add(final LinkNode<Entry<K, V>> node) {
        if (this.keyToNodeMap.size() >= this.capacity) {
            final LinkNode<Entry<K, V>> nodeToRemove = this.queue.removeFront();
            this.remove(nodeToRemove);
        }
        this.keyToNodeMap.put(node.contents().key(), node);
        this.queue.addBack(node);
    }

    private void remove(final LinkNode<Entry<K, V>> node) {
        node.remove();
        this.keyToNodeMap.remove(node.contents().key);
    }
}
