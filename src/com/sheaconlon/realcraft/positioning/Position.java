package com.sheaconlon.realcraft.positioning;

import com.sheaconlon.realcraft.world.Chunk;

/**
 * A position in 3D space.
 */
public class Position extends ThreeVector {
    /**
     * The zero position, the position whose coordinates are all zero.
     */
    public static final Position ZERO = new Position(0, 0, 0);

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
     * Get the absolute x-coordinate of this position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute x-coordinate of this position, if it is relative to the reference position.
     */
    public double getXAbsolute(final Position reference) {
        return reference.getX() + this.getX();
    }

    /**
     * Get the absolute y-coordinate of this position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute y-coordinate of this position, if it is relative to the reference position.
     */
    public double getYAbsolute(final Position reference) {
        return reference.getY() + this.getY();
    }

    /**
     * Get the absolute z-coordinate of this position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute z-coordinate of this position, if it is relative to the reference position.
     */
    public double getZAbsolute(final Position reference) {
        return reference.getZ() + this.getZ();
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
