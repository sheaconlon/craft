package com.sheaconlon.realcraft.positioning;

import com.sheaconlon.realcraft.world.Chunk;

public class BlockPosition extends IntPosition {
    /**
     * The block position whose coordinates are all zeros.
     */
    public static final BlockPosition ZERO = new BlockPosition(0, 0, 0);

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
     * Return the position at the low-x, low-y, low-z corner of this block position.
     * @return The position at the low-x, low-y, low-z corner of this block position.
     */
    public Position toPosition() {
        return new Position(
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
                Position.floorCoordinate((double)this.getX() / (double)Chunk.SIZE),
                Position.floorCoordinate((double)this.getY() / (double)Chunk.SIZE),
                Position.floorCoordinate((double)this.getZ() / (double)Chunk.SIZE)
        );
    }
}
