package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.LinkedList;
import java.util.List;

/**
 * A cubical subset of the world.
 */
public class Chunk implements Renderable {
    // TODO: Tune chunk size.
    /**
     * The side length of chunks, in meters.
     */
    public static final int SIZE = 100;

    /**
     * The x-coordinate of this chunk.
     *
     * This chunk represents the components of the world with x coordinate such that this.x &lt;= x &lt; this.x + Chunk.SIZE.
     */
    private final int x;

    /**
     * The y-coordinate of this chunk.
     *
     * This chunk represents the components of the world with y coordinate such that this.y &lt;= y &lt; this.y + Chunk.SIZE.
     */
    private final int y;

    /**
     * The z-coordinate of this chunk.
     *
     * This chunk represents the components of the world with z coordinate such that this.z &lt;= z &lt; this.z + Chunk.SIZE.
     */
    private final int z;

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

    static int blockToChunkCoordinate(final int blockCoordinate) {
        return (int)Math.floor((double)blockCoordinate / (double)Chunk.SIZE);
    }

    /**
     * Construct a chunk.
     *
     * @param x See {@link #x}.
     * @param y See {@link #y}.
     * @param z See {@link #z}.
     */
    Chunk(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
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
     * Set the block at position (x, y, z).
     * @param x The x-coordinate to place the block at.
     * @param y The y-coordinate to place the block at.
     * @param z The z-coordinate to place the block at.
     * @param block The block.
     */
    void setBlock(final int x, final int y, final int z, final Block block) {
        this.blocks[x - this.x][y - this.y][z - this.z] = block;
    }

    /**
     * Get the block at position (x, y, z).
     * @param x The x-coordinate of the desired block.
     * @param y The y-coordinate of the desired block.
     * @param z The z-coordinate of the desired block.
     * @return The desired block.
     */
    Block getBlock(final int x, final int y, final int z) {
        return this.blocks[x - this.x][y - this.y][z - this.z];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getX() {
        return this.x;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getY() {
        return this.y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getZ() {
        return this.z;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Quad> getQuads() {
        final List<Quad> quads = new LinkedList<>();
        for (int bx = this.x; bx < this.x + Chunk.SIZE; bx++) {
            for (int by = this.y; by < this.y + Chunk.SIZE; by++) {
                for (int bz = this.z; bz < this.z + Chunk.SIZE; bz++) {
                    for (final Quad quad : this.getBlock(bx, by, bz).getQuads()) {
                        quads.add(quad);
                    }
                }
            }
        }
        return quads;
    }
}
