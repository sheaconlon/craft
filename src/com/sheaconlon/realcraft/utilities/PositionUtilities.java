package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;

/**
 * A collection of position utilities.
 */
public class PositionUtilities {
    /**
     * Get the position of the anchor point of the chunk containing some position.
     * @return The position of the anchor point of the chunk containing some position.
     */
    public static int[] toChunkPosition(final double[] pos) {
       final int[] chunkPos = new int[3];
       for (int i = 0; i < 3; i++) {
           chunkPos[i] = Chunk.SIZE * PositionUtilities.floorCoordinate(pos[i] / Chunk.SIZE);
       }
       return chunkPos;
    }

    /**
     * Floor a coordinate.
     *
     * The floor of a coordinate is the greatest integer which is less than or equal to the coordinate.
     * @param coord The coordinate.
     * @return The floor of the coordinate.
     */
    private static int floorCoordinate(final double coord) {
        return (int)Math.floor(coord);
    }
}
