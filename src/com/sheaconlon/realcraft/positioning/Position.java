package com.sheaconlon.realcraft.positioning;

import com.sheaconlon.realcraft.world.Chunk;

/**
 * A position in 3D space.
 */
public class Position extends ThreeVector {
    /**
     * Construct a vector.
     * @param x The x-coordinate of the vector.
     * @param y The y-coordinate of the vector.
     * @param z The z-coordinate of the vector.
     */
    public Position(final double x, final double y, final double z) {
        super(x, y, z);
    }

    /**
     * Construct a vector which is the same as some other vector.
     * @param pos The other vector.
     */
    public Position(final Position pos) {
        super(pos);
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

    /**
     * Floor a coordinate.
     * @param coordinate The coordinate.
     * @return The largest integer that is still less than or equal to {@code coordinate}.
     */
    static long floorCoordinate(final double coordinate) {
        return (long)Math.floor(coordinate);
    }
}
