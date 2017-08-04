package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

/**
 * A cubical subset of the world.
 */
public class Chunk {
    // ##### PRIVATE STATIC #####
    private static final int BLOCKS = 15;
    private static final double SIZE = BLOCKS * Block.SIZE;
    private static final Map<Vector, Chunk> chunks = new HashMap<>();

    // ##### PRIVATE FINAL #####
    private final Vector anchor;
    private final Block[][][] blocks;
    private final Set<Entity> entities;

    // ##### CONSTRUCTORS #####
    /**
     * Create a chunk.
     * @param anchor Its anchor point.
     */
    private Chunk(final Vector anchor) {
        this.anchor = anchor;
        this.blocks = new Block[BLOCKS][BLOCKS][BLOCKS];
        this.entities = new HashSet<>();
        for (final Vector position : this.blockAnchors()) {
            this.putBlock(new AirBlock(position));
        }
    }

    // ##### BLOCKS #####
    /**
     * Get the block with some anchor point.
     * @param anchor The anchor point.
     * @return The block with anchor point {@code anchor}.
     */
    public Block getBlock(final Vector anchor) {
        final Vector relativePosition = Vector.subtract(anchor, this.anchor);
        return this.blocks[relativePosition.getXInt()][relativePosition.getYInt()][relativePosition.getZInt()];
    }

    /**
     * Put a block.
     * @param block The block.
     */
    public void putBlock(final Block block) {
        final Vector relativePosition = Vector.subtract(block.getPos(), this.anchor);
        this.blocks[relativePosition.getXInt()][relativePosition.getYInt()][relativePosition.getZInt()] = block;
    }

    // ##### ENTITIES #####
    /**
     * Add an entity to this chunk.
     * @param entity The entity.
     */
    public void addEntity(final Entity entity) {
        this.entities.add(entity);
    }

    /**
     * Remove an entity from this chunk.
     * @param entity The entity.
     */
    public void removeEntity(final Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Get the entities in this chunk.
     * @return The entities in this chunk.
     */
    public Collection<Entity> getEntities() {
        return this.entities;
    }

    // ##### ANCHORS #####
    private class BlockAnchors implements Iterable<Vector> {
        private class BlockAnchorsIterator implements Iterator<Vector> {
            private Iterator<Vector> integerLattice;

            BlockAnchorsIterator() {
                this.integerLattice = Vector.between(Vector.ZERO, Vector.uniform(BLOCKS - 1)).iterator();
            }

            @Override
            public boolean hasNext() {
                return this.integerLattice.hasNext();
            }

            @Override
            public Vector next() {
                return Vector.add(
                        Chunk.this.anchor,
                        Vector.scale(
                                this.integerLattice.next(),
                                Block.SIZE
                        )
                );
            }
        }

        @Override
        public Iterator<Vector> iterator() {
            return new BlockAnchorsIterator();
        }
    }

    /**
     * Get the anchor points of the blocks in this chunk.
     * @return The anchor points of the blocks in this chunk.
     */
    public Iterable<Vector> blockAnchors() {
        return new BlockAnchors();
    }

    // ##### CHUNKS #####
    /**
     * Return the chunk containing some position.
     * @param position The position.
     * @return The chunk containing {@code pos}.
     */
    public static Chunk containingChunk(final Vector position) {
        final Vector anchor = Vector.scale(
            Vector.round(
                Vector.scale(position, 1 / SIZE)
            ),
            SIZE
        );
        return getChunk(anchor);
    }

    private class ChunksNearby implements Iterable<Chunk> {
        private class ChunksNearbyIterator implements Iterator<Chunk> {
            private Iterator<Vector> chunkAnchorsNearby;

            ChunksNearbyIterator() {
                this.chunkAnchorsNearby = Vector.around(Chunk.this.anchor, ChunksNearby.this.distance).iterator();
            }

            @Override
            public boolean hasNext() {
                return this.chunkAnchorsNearby.hasNext();
            }

            @Override
            public Chunk next() {
                return getChunk(this.chunkAnchorsNearby.next());
            }
        }

        private final int distance;

        ChunksNearby(final int distance) {
            this.distance = distance;
        }

        @Override
        public Iterator<Chunk> iterator() {
            return new ChunksNearbyIterator();
        }
    }

    /**
     * Get the chunks nearby this chunk.
     *
     * The distance between chunks is measured between their anchor points.
     * @param distance The maximum distance at which a chunk should be included.
     * @return The chunks nearby this chunk.
     */
    public Iterable<Chunk> chunksNearby(final int distance) {
        return new ChunksNearby(distance);
    }

    // ##### OVERRIDES OF OBJECT #####
    @Override
    public int hashCode() {
        return this.anchor.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (!(object instanceof Chunk)) {
            return false;
        }
        final Chunk otherChunk = (Chunk)object;
        return this.anchor.equals(otherChunk.anchor);
    }

    // ##### PRIVATE STATIC #####
    private static Chunk getChunk(final Vector anchor) {
        if (!Vector.scale(anchor, 1 / SIZE).isInt()) {
            throw new IllegalArgumentException("Anchor point given is not the anchor point of any chunk.");
        }
        Chunk chunk = chunks.get(anchor);
        if (chunk == null) {
            chunk = new Chunk(anchor);
            chunks.put(anchor, chunk);
        }
        return chunk;
    }
}
