package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entities.Entity;

import java.util.LinkedList;
import java.util.List;

/**
 * A cubical subset of the world.
 */
public abstract class Chunk {
    // TODO: Tune chunk size.
    /**
     * The side length of chunks, in meters.
     */
    private static final int SIZE = 100;

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
}
