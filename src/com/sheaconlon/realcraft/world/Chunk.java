package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.Entity;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.positioning.ChunkPosition;

import java.util.LinkedList;
import java.util.List;

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
         * The {@link ChunkBlockPositionQuadIterator} for {@link #chunk} and {@link #currentPosition}.
         */
        private Iterator<Quad> currentIterator;

        /**
         * Construct a chunk quad iterator.
         * @param chunk The chunk to iterate over the quads of.
         */
        ChunkQuadIterator(final Chunk chunk) {
            this.chunk = chunk;
            this.currentPosition = chunk.getPosition().toBlockPosition();
            this.currentIterator = new ChunkBlockPositionQuadIterator(this.chunk, this.currentPosition);
            this.advancePosition();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            if (this.currentIterator.hasNext()) {
                return true;
            }
            this.advancePosition();
            return this.currentIterator.hasNext();
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
        private void advancePosition() {
            this.advancePositionZ();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new ChunkBlockPositionQuadIterator(this.chunk, this.currentPosition);
                return;
            }
            this.advancePositionY();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new ChunkBlockPositionQuadIterator(this.chunk, this.currentPosition);
                return;
            }
            this.advancePositionX();
            if (this.chunk.containsPosition(this.currentPosition)) {
                this.currentIterator = new ChunkBlockPositionQuadIterator(this.chunk, this.currentPosition);
            }
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

    // TODO: Tune chunk size.
    /**
     * The side length of chunks, in meters.
     */
    public static final int SIZE = 100;

    // TODO: Fix this.
    /**
     * The x-coordinate of this chunk.
     *
     * This chunk represents the components of the world with x coordinate such that this.x &lt;= x &lt; this.x +
     * Chunk.SIZE, y coordinate such that this.y &lt;= y &lt; this.y + Chunk.SIZE, and z coordinate such that
     * this.z &lt;= z &lt; this.z + Chunk.SIZE.
     */
    private final ChunkPosition pos;

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
     * @param pos See {@link #pos}.
     */
    Chunk(final ChunkPosition pos) {
        this.pos = new ChunkPosition(pos);
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
        final Position chunkPosition = this.getPosition();
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

    // TODO: Cache this.pos.toBlockPosition()

    /**
     * Return whether this chunk contains some position.
     * @param position The position.
     * @return Whether this chunk contains the position.
     */
    boolean containsPosition(final BlockPosition position) {
        final BlockPosition anchor = this.pos.toBlockPosition();
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
        final BlockPosition relativePos = this.makeRelative(pos);
        this.blocks[(int)relativePos.getX()][(int)relativePos.getY()][(int)relativePos.getZ()] = block;
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block.
     */
    Block getBlock(final BlockPosition pos) {
        final BlockPosition relativePos = this.makeRelative(pos);
        return this.blocks[(int)relativePos.getX()][(int)relativePos.getY()][(int)relativePos.getZ()];
    }

    // TODO: Add notion of anchors throughout codebase.
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
        final BlockPosition relativePosition = this.makeRelative(position);
        return this.entities[(int)relativePosition.getX()][(int)relativePosition.getY()]
                [(int)relativePosition.getZ()];
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
     * {@inheritDoc}
     */
    @Override
    public Position getPosition() {
        return this.pos.toPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Quad> getQuads() {
        final List<Quad> quads = new LinkedList<>();
        for (long bx = this.pos.getX(); bx < this.pos.getX() + Chunk.SIZE; bx++) {
            for (long by = this.pos.getY(); by < this.pos.getY() + Chunk.SIZE; by++) {
                for (long bz = this.pos.getZ(); bz < this.pos.getZ() + Chunk.SIZE; bz++) {
                    final BlockPosition blockPos = new BlockPosition(bx, by, bz);
                    for (final Quad quad : this.getBlock(blockPos).getQuads()) {
                        quads.add(quad);
                    }
                }
            }
        }
        return quads;
    }

    /**
     * Add an entity to this chunk.
     * @param newEntity The entity.
     */
    public void addEntity(final Entity newEntity) {
        final BlockPosition position = newEntity.getPosition().toBlockPosition();
        // TODO: Use ArrayList<LinkedList> instead of LinkedList[] so that long block positions will be supported.
        this.entities[(int)position.getX()][(int)position.getY()][(int)position.getZ()].add(newEntity);
    }

    private BlockPosition makeRelative(final BlockPosition pos) {
        final BlockPosition result = new BlockPosition(
                pos.getX() - this.pos.getX(),
                pos.getY() - this.pos.getY(),
                pos.getZ() - this.pos.getZ()
        );
        if (result.getX() < 0 || result.getX() >= Chunk.SIZE) {
            throw new IllegalArgumentException("position is not within this chunk");
        }
        if (result.getY() < 0 || result.getY() >= Chunk.SIZE) {
            throw new IllegalArgumentException("position is not within this chunk");
        }
        if (result.getZ() < 0 || result.getZ() >= Chunk.SIZE) {
            throw new IllegalArgumentException("position is not within this chunk");
        }
        return result;
    }

    /**
     * Return the hash code of this chunk.
     *
     * The hash code of a chunk is the hash code of its chunk position.
     * @return The hash code of this chunk.
     */
    @Override
    public int hashCode() {
        return this.getPosition().hashCode();
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
        return this.getPosition().equals(otherChunk.getPosition());
    }
}
