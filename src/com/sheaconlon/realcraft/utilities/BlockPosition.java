package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;

public class BlockPosition extends IntPosition {
    /**
     * Construct a block position.
     * @param x The x-coordinate of the block position.
     * @param y The y-coordinate of the block position.
     * @param z The z-coordinate of the block position.
     */
    public BlockPosition(final long x, final long y, final long z) {
        super(x, y, z);
    }

    /**
     * Construct a block position which is the same as some other block position.
     * @param pos The other block position
     */
    public BlockPosition(final BlockPosition pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Return the entity position at the low-x, low-y, low-z corner of this block position.
     * @return The entity position at the low-x, low-y, low-z corner of this block position.
     */
    public EntityPosition toEntityPosition() {
        return new EntityPosition(
                this.getX(),
                this.getY(),
                this.getZ()
        );
    }

    /**
     * Return the chunk position of the chunk containing this position.
     * @return The chunk position of the chunk containing this position.
     */
    public ChunkPosition toChunkPosition() {
        return new ChunkPosition(
                Position.floorCoordinate(this.getX() / Chunk.SIZE),
                Position.floorCoordinate(this.getY() / Chunk.SIZE),
                Position.floorCoordinate(this.getZ() / Chunk.SIZE)
        );
    }
}
