package com.sheaconlon.realcraft.utilities;

import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of position utilities.
 */
public class PositionUtilities {
    /**
     * Get the position of the anchor point of the block containing some position.
     * @param pos The position.
     * @return The position of the anchor point of the block containing the position.
     */
    public static int[] toBlockPosition(final double[] pos) {
        final int[] blockPos = new int[3];
        for (int i = 0; i < 3; i++) {
            blockPos[i] = PositionUtilities.floorCoordinate(pos[i]);
        }
        return blockPos;
    }

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
     * Get the position of the anchor point of the chunk containing some position.
     * @return The position of the anchor point of the chunk containing some position.
     */
    public static int[] toChunkPosition(final int[] pos) {
        final int[] chunkPos = new int[3];
        for (int i = 0; i < 3; i++) {
            chunkPos[i] = Chunk.SIZE * PositionUtilities.floorCoordinate((double)pos[i] / Chunk.SIZE);
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
     * A comparator of chunk positions based on their weighted taxicab distance from a chunk of interest.
     */
    private static class ChunkPositionComparator implements Comparator<int[]> {
        /**
         * Weights for the distance metric used by a chunk position comparator.
         */
        private static final double[] WEIGHTS = new double[]{1, 1.5, 1};

        /**
         * The position of the anchor point of the chunk of interest.
         */
        private final int[] pos;

        /**
         * Create a chunk position comparator.
         * @param pos See {@link #pos}.
         */
        ChunkPositionComparator(final int[] pos) {
            this.pos = pos;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(int[] a, int[] b) {
            final int[] diffA = ArrayUtilities.subtract(a, this.pos);
            final int[] absDiffA = ArrayUtilities.abs(diffA);
            final double[] weightedDiffA = ArrayUtilities.multiply(absDiffA, ChunkPositionComparator.WEIGHTS);
            final double distA = ArrayUtilities.sum(weightedDiffA);
            final int[] diffB = ArrayUtilities.subtract(b, this.pos);
            final int[] absDiffB = ArrayUtilities.abs(diffB);
            final double[] weightedDiffB = ArrayUtilities.multiply(absDiffB, ChunkPositionComparator.WEIGHTS);
            final double distB = ArrayUtilities.sum(weightedDiffB);
            return Double.compare(distA, distB);
        }
    }

    /**
     * Get the positions of the anchor points of the chunks which are "nearby" some chunk of interest, in ascending
     * order of some distance metric.
     * @param pos The position of the anchor point of the chunk of interest.
     * @param chunkRadius The number of chunks in each direction that should be considered "nearby" a chunk.
     * @return The positions of the anchor points of the chunks which are "nearby" some chunk of interest.
     */
    public static Iterable<int[]> getNearbyChunkPositions(final int[] pos, final int chunkRadius) {
        final List<int[]> positions = new ArrayList<>();
        for (int x = pos[0] - chunkRadius * Chunk.SIZE; x <= pos[0] + chunkRadius * Chunk.SIZE; x += Chunk.SIZE) {
            for (int y = pos[1] - chunkRadius * Chunk.SIZE; y <= pos[1] + chunkRadius * Chunk.SIZE; y += Chunk.SIZE) {
                for (int z = pos[2] - chunkRadius * Chunk.SIZE; z <= pos[2] + chunkRadius * Chunk.SIZE; z += Chunk.SIZE) {
                    positions.add(new int[]{x, y, z});
                }
            }
        }
        positions.sort(new PositionUtilities.ChunkPositionComparator(pos));
        return positions;
    }

    /**
     * Translate some positions.
     * @param positions The positions.
     * @param translation The translation vector.
     * @return New positions which are {@code positions} translated by {@code translation}.
     */
    public static double[][] translatePositions(final double[][] positions, final double[] translation) {
        final double[][] translatedPositions = new double[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            translatedPositions[i] = ArrayUtilities.add(translation, positions[i]);
        }
        return translatedPositions;
    }

    /**
     * Scale some positions.
     * @param positions The positions.
     * @param scalars The scaling factors along the x, y, and z dimensions.
     * @return New positions which are {@code positions} scaled by {@code scalars}.
     */
    public static double[][] scalePositions(final double[][] positions, final double[] scalars) {
        final double[][] scaledPositions = new double[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = ArrayUtilities.multiply(scalars, positions[i]);
        }
        return scaledPositions;
    }

    /**
     * Rotate some positions.
     * @param positions The positions.
     * @param xzOrientationDelta See {@link WorldObject#xzOrientation}.
     * @param xzCrossOrientationDelta See {@link WorldObject#xzCrossOrientation}.
     * @return New positions which are {@code positions} rotated by {@code xzOrientationDelta} and
     * {@code xzCrossOrientationDelta}.
     */
    public static double[][] rotatePositions(final double[][] positions, final double xzOrientationDelta,
                                             final double xzCrossOrientationDelta) {
        final double[][] rotatedPositions = new double[positions.length][];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new double[]{
                    positions[i][0] * Math.cos(xzOrientationDelta) * Math.cos(xzCrossOrientationDelta),
                    positions[i][1] * Math.sin(xzCrossOrientationDelta),
                    positions[i][2] * Math.sin(xzOrientationDelta) * Math.cos(xzCrossOrientationDelta)
            };
        }
        return rotatedPositions;
    }
}
