package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;

import java.util.LinkedList;
import java.util.List;

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

    /**
     * Get the positions of the anchor points of the chunks which are "nearby" some chunk of interest.
     * @param pos The position of the anchor point of the chunk of interest.
     * @param chunkRadius The number of chunks in each direction that should be considered "nearby" a chunk.
     * @return The positions of the anchor points of the chunks which are "nearby" some chunk of interest.
     */
    public static Iterable<int[]> getNearbyChunkPositions(final int[] pos, final int chunkRadius) {
        final List<int[]> positions = new LinkedList<>();
        for (int x = pos[0] - chunkRadius * Chunk.SIZE; x <= pos[0] + chunkRadius * Chunk.SIZE; x += Chunk.SIZE) {
            for (int y = pos[1] - chunkRadius * Chunk.SIZE; y <= pos[1] + chunkRadius * Chunk.SIZE; y += Chunk.SIZE) {
                for (int z = pos[2] - chunkRadius * Chunk.SIZE; z <= pos[2] + chunkRadius * Chunk.SIZE; z += Chunk.SIZE) {
                    positions.add(new int[]{x, y, z});
                }
            }
        }
        return positions;
    }
}
