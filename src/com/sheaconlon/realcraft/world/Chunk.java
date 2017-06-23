package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.Entity;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.positioning.ChunkPosition;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A cubical subset of the world.
 */
public class Chunk implements Renderable {
    /**
     * An iterator over quads in a chunk.
     */
    private class ChunkQuadIterator implements Iterator<Quad> {
        /**
         * The chunk to iterate over the quads of.
         */
        private final Chunk chunk;

        /**
         * The block position from which we are currently getting quads.
         */
        private BlockPosition currentPosition;

        /**
         * The {@link BlockPositionQuadIterator} for {@link #chunk} and {@link #currentPosition}.
         */
        private Iterator<Quad> currentIterator;

        /**
         * Construct a chunk quad iterator.
         * @param chunk The chunk to iterate over the quads of.
         */
        ChunkQuadIterator(final Chunk chunk) {
            this.chunk = chunk;
            this.currentPosition = new BlockPosition(chunk.getBlockAnchor());
            this.currentIterator = new BlockPositionQuadIterator(this.chunk, this.currentPosition);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (this.currentIterator.hasNext()) {
                return true;
            }
            final boolean success = this.advancePosition();
            if (!success) {
                return false;
            }
            return this.hasNext();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Quad next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.currentIterator.next();
        }

        /**
         * Advance the position from which we are getting quads.
         */
        private boolean advancePosition() {
            this.advancePositionZ();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new BlockPositionQuadIterator(this.chunk, this.currentPosition);
                return true;
            }
            this.advancePositionY();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new BlockPositionQuadIterator(this.chunk, this.currentPosition);
                return true;
            }
            this.advancePositionX();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new BlockPositionQuadIterator(this.chunk, this.currentPosition);
                return true;
            }
            return false;
        }

        /**
         * Advance the position from which we are getting quads along the z-axis.
         */
        private void advancePositionZ() {
            this.currentPosition.changeZ(1);
        }

        /**
         * Advance the position from which we are getting quads along the y-axis.
         */
        private void advancePositionY() {
            this.currentPosition.changeY(1);
            this.currentPosition.setZ(0);
        }

        /**
         * Advance the position from which we are getting quads along the x-axis.
         */
        private void advancePositionX() {
            this.currentPosition.changeX(1);
            this.currentPosition.setY(0);
        }
    }


    /**
     * An iterator over quads in a block position in a chunk.
     */
    private class BlockPositionQuadIterator implements Iterator<Quad> {
        /**
         * The chunk which contains the block position we are iterating over the quads of.
         */
        private final Chunk chunk;

        /**
         * The block position we are iterating over the quads of.
         */
        private final BlockPosition position;

        /**
         * The iterator over {@link #position}'s block's quads.
         */
        private final Iterator<Quad> blockQuadIterator;

        /**
         * The iterator over {@link #position}'s entities.
         */
        private final Iterator<Entity> entityIterator;

        /**
         * The iterator over the current entity's quads.
         */
        private Iterator<Quad> entityQuadIterator;

        /**
         * Construct a chunk block position quad iterator.
         * @param chunk The chunk in which {#code position} is located.
         * @param position The position to iterate over the quads of.
         */
        BlockPositionQuadIterator(final Chunk chunk, final BlockPosition position) {
            this.chunk = chunk;
            this.position = new BlockPosition(position);
            this.blockQuadIterator = this.chunk.getBlock(this.position).iterator();
            this.entityIterator = this.chunk.getEntities(this.position).iterator();
            this.entityQuadIterator = null;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (this.blockQuadIterator.hasNext()) {
                return true;
            }
            if (this.entityQuadIterator != null && this.entityQuadIterator.hasNext()) {
                return true;
            }
            if (this.entityIterator.hasNext()) {
                this.entityQuadIterator = this.entityIterator.next().iterator();
                return this.hasNext();
            }
            return false;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Quad next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.blockQuadIterator.hasNext()) {
                return this.blockQuadIterator.next();
            }
            return this.entityQuadIterator.next();
        }
    }

    // TODO: Tune chunk size.
    /**
     * The side length of chunks, in meters.
     */
    public static final int SIZE = 25;

    /**
     * The chunk position of this chunk.
     */
    private final ChunkPosition chunkPosition;

    /**
     * The anchor of this chunk, as a block position.
     * @see Physical#anchor
     */
    private final BlockPosition blockAnchor;

    /**
     * @see Physical#anchor
     */
    private final Position anchor;

    /**
     * The blocks in this chunk.
     *
     * The block at (x, y, z) is stored as {@code this.blocks[x - this.x][y - this.y][z - this.z]}.
     */
    private final Block[][][] blocks;

    /**
     * The entities in this chunk, stored as a list of entities within every "block space".
     *
     * If an entity {@code e} fills the point (x, y, z), then the value of
     * {@code this.entities[(int)Math.floor(x)][(int)Math.floor(y)][(int)Math.floor(z)].contains(e)} is {@code true}.
     */
    private final List<Entity>[][][] entities;

    /**
     * Construct a chunk.
     *
     * @param chunkPosition See {@link #chunkPosition}.
     */
    Chunk(final ChunkPosition chunkPosition) {
        this.chunkPosition = new ChunkPosition(chunkPosition);
        this.blockAnchor = chunkPosition.toBlockPosition();
        this.anchor = chunkPosition.toPosition();
        this.blocks = new Block[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
        // TODO: Reevaluate choice of LinkedList over ArrayList.
        this.entities = new LinkedList[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
        for (int cx = 0; cx < Chunk.SIZE; cx++) {
            for (int cy = 0; cy < Chunk.SIZE; cy++) {
                for (int cz = 0; cz < Chunk.SIZE; cz++) {
                    this.entities[cx][cy][cz] = new LinkedList<>();
                }
            }
        }
    }

    /**
     * Return whether this chunk contains some position.
     * @param position The position.
     * @return Whether this chunk contains the position.
     */
    boolean containsPosition(final Position position) {
        final Position chunkPosition = this.getAnchor();
        final double xRelative = position.getXRelative(chunkPosition);
        if (xRelative < 0 || xRelative >= Chunk.SIZE) {
            return false;
        }
        final double yRelative = position.getYRelative(chunkPosition);
        if (yRelative < 0 || yRelative >= Chunk.SIZE) {
            return false;
        }
        final double zRelative = position.getZRelative(chunkPosition);
        if (zRelative < 0 || zRelative >= Chunk.SIZE) {
            return false;
        }
        return true;
    }

    /**
     * Return whether this chunk contains some position.
     * @param position The position.
     * @return Whether this chunk contains the position.
     */
    boolean containsPosition(final BlockPosition position) {
        final BlockPosition anchor = this.getBlockAnchor();
        final long xRelative = position.getXRelative(anchor);
        if (xRelative < 0 || xRelative >= Chunk.SIZE) {
            return false;
        }
        final long yRelative = position.getYRelative(anchor);
        if (yRelative < 0 || yRelative >= Chunk.SIZE) {
            return false;
        }
        final long zRelative = position.getZRelative(anchor);
        if (zRelative < 0 || zRelative >= Chunk.SIZE) {
            return false;
        }
        return true;
    }

    /**
     * Put a block at some position.
     * @param pos The position.
     * @param block The block.
     */
    void setBlock(final BlockPosition pos, final Block block) {
        final BlockPosition anchor = this.getBlockAnchor();
        this.blocks[(int)pos.getXRelative(anchor)][(int)pos.getYRelative(anchor)]
                [(int)pos.getZRelative(anchor)] = block;
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block.
     */
    Block getBlock(final BlockPosition pos) {
        final BlockPosition anchor = this.getBlockAnchor();
        return this.blocks[(int)pos.getXRelative(anchor)][(int)pos.getYRelative(anchor)]
                [(int)pos.getZRelative(anchor)];
    }

    /**
     * Get the block at some position relative to the return value of {@code #getPosition()}.
     * @param position The position.
     * @return The block.
     */
    Block getBlockRelative(final BlockPosition position) {
        return this.blocks[(int)position.getX()][(int)position.getY()][(int)position.getZ()];
    }

    /**
     * Get a list of the entities within some block position.
     * @param position The block position.
     * @return A list of the entities within the block position.
     */
    List<Entity> getEntities(final BlockPosition position) {
        final BlockPosition anchor = this.getBlockAnchor();
        return this.entities[(int)position.getXRelative(anchor)][(int)position.getYRelative(anchor)]
                [(int)position.getZRelative(anchor)];
    }

    /**
     * Get a list of the entities within some block position relative to the return value of
     * {@code #getPosition()}.
     * @param position The block position.
     * @return A list of the entities within the block position.
     */
    List<Entity> getEntitiesRelative(final BlockPosition position) {
        return this.entities[(int)position.getX()][(int)position.getY()][(int)position.getZ()];
    }

    /**
     * Getter for {@link #chunkPosition}.
     */
    public ChunkPosition getChunkPosition() {
        return this.chunkPosition;
    }

    /**
     * Getter for {@link #blockAnchor}.
     */
    public BlockPosition getBlockAnchor() {
        return this.blockAnchor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Position getAnchor() {
        return this.anchor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Quad> iterator() {
        return new ChunkQuadIterator(this);
    }

    /**
     * Add an entity to this chunk.
     * @param newEntity The entity.
     */
    public void addEntity(final Entity newEntity) {
        final BlockPosition anchor = this.getBlockAnchor();
        final BlockPosition position = newEntity.getAnchor().toBlockPosition();
        this.entities[(int)position.getXRelative(anchor)][(int)position.getYRelative(anchor)]
                [(int)position.getZRelative(anchor)].add(newEntity);
    }

    /**
     * Return the hash code of this chunk.
     *
     * The hash code of a chunk is the hash code of its chunk position.
     * @return The hash code of this chunk.
     */
    @Override
    public int hashCode() {
        return this.getChunkPosition().hashCode();
    }

    /**
     * Return whether this chunk equals some other chunk.
     *
     * Two chunks are equal if their chunk positions are the same.
     * @param other The other chunk.
     * @return Whether this chunk equals the other chunk.
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Chunk)) {
            return false;
        }
        final Chunk otherChunk = (Chunk)other;
        return this.getChunkPosition().equals(otherChunk.getChunkPosition());
    }
}
