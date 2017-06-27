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
    public static int[] toChunkPosition(final double[] position) {
       final int[] chunkPosition = new int[position.length];
       for (int i = 0; i < position.length; i++) {
           chunkPosition[i] = (int)Math.floor(position[i] / Chunk.SIZE);
       }
       return chunkPosition;
    }
}
