package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;

public class EntityPosition extends Position {
    /**
     * Construct a position.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @param z The z-coordinate of the position.
     */
    public EntityPosition(final double x, final double y, final double z) {
        super(x, y, z);
    }

    /**
     * Return the block position of the block containing this position.
     * @return The block position of the block containing this position.
     */
    public BlockPosition toBlockPosition() {
        return new BlockPosition(
                Position.floorCoordinate(this.getX()),
                Position.floorCoordinate(this.getY()),
                Position.floorCoordinate(this.getZ())
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
