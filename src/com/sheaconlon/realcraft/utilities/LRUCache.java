package com.sheaconlon.realcraft.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * An LRU cache.
 */
public class LRUCache<K, V> {
    private class Entry {
        final K key;
        final V value;

        public Entry(final K key, final V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, LinkNode<LRUCache<K, V>.Entry>> keyNodeMap;
    private final ExposedLinkedList<LRUCache<K, V>.Entry> nodeList;

    /**
     * Create an LRU cache.
     * @param capacity The capacity of the cache.
     */
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.keyNodeMap = new HashMap<>();
        this.nodeList = new ExposedLinkedList<>();
    }

    /**
     * Get the value for some key.
     * @param key The key.
     * @return The value.
     */
    public V get(final K key) {
        final LinkNode<LRUCache<K, V>.Entry> node = this.keyNodeMap.get(key);
        if (node == null) {
            return null;
        }
        this.remove(node);
        this.add(key, node.contents().value);
        return node.contents().value;
    }

    /**
     * Set the value associated with some key, adding it if not already present.
     * @param key The key.
     * @param value The new value.
     */
    public void put(final K key, final V value) {
        final LinkNode<LRUCache<K, V>.Entry> node = this.keyNodeMap.get(key);
        if (node != null) {
            this.remove(node);
        }
        this.add(key, value);
    }

    private void add(final K key, final V value) {
        if (this.keyNodeMap.size() >= this.capacity) {
            final LinkNode<LRUCache<K, V>.Entry> nodeToRemove = this.nodeList.removeFront();
            this.remove(nodeToRemove);
        }
        final LRUCache<K, V>.Entry entry = new LRUCache<K, V>.Entry(key, value);
        LinkNode<LRUCache<K, V>.Entry> node = new LinkNode<>(entry);
        this.keyNodeMap.put(key, node);
        this.nodeList.addBack(node);
    }

    private void remove(final LinkNode<LRUCache<K, V>.Entry> node) {
        node.remove();
        this.keyNodeMap.remove(node.contents().key);
    }
}
