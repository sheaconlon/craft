package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;

public class ChunkPosition extends IntPosition {
    /**
     * Construct a chunk position.
     * @param x The x-coordinate of the chunk position.
     * @param y The y-coordinate of the chunk position.
     * @param z The z-coordinate of the chunk position.
     */
    public ChunkPosition(final long x, final long y, final long z) {
        super(x, y, z);
    }

    /**
     * Return the entity position at the low-x, low-y, low-z corner of this chunk position.
     * @return The entity position at the low-x, low-y, low-z corner of this chunk position.
     */
    public EntityPosition toEntityPosition() {
        return new EntityPosition(
                this.getX() * Chunk.SIZE,
                this.getY() * Chunk.SIZE,
                this.getZ() * Chunk.SIZE
        );
    }

    /**
     * Return the block position at the low-x, low-y, low-z corner of this chunk position.
     * @return The block position at the low-x, low-y, low-z corner of this chunk position.
     */
    public BlockPosition toBlockPosition() {
        return new BlockPosition(
                this.getX() * Chunk.SIZE,
                this.getY() * Chunk.SIZE,
                this.getZ() * Chunk.SIZE
        );
    }
}