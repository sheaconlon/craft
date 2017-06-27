package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.world.Chunk;

/**
 * A block, a cubical, grid-aligned object in the world.
 */
public abstract class Block extends WorldObject {
    /**
     * The normal vector for the front face of a block.
     */
    private static final float[] FRONT_NORMAL = new float[]{0, 0, 1};

    /**
     * The positions of the vertices of the front face of a block, relative to its anchor.
     */
    private static final float[][] FRONT_POSITIONS = new float[][]{
            new float[]{0, 0, 0},
            new float[]{1, 0, 0},
            new float[]{1, 1, 0},
            new float[]{0, 1, 0}
    };

    /**
     * The normal vector for the left face of a block.
     */
    private static final float[] LEFT_NORMAL = new float[]{-1, 0, 0};

    // TODO: Ensure that attributes are fully qualified throughout codebase.

    /**
     * The positions of the vertices of the left face of a block, relative to its anchor.
     */
    private static final float[][] LEFT_POSITIONS = new float[][]{
            new float[]{0, 0, -1},
            new float[]{0, 0, 0},
            new float[]{0, 1, 0},
            new float[]{0, 1, -1}
    };

    /**
     * The normal vector for the back face of a block.
     */
    private static final float[] BACK_NORMAL = new float[]{0, 0, -1};

    /**
     * The positions of the vertices of the back face of a block, relative to its anchor.
     */
    private static final float[][] BACK_POSITIONS = new float[][]{
            new float[]{1, 0, -1},
            new float[]{0, 0, -1},
            new float[]{0, 1, -1},
            new float[]{1, 1, -1}
    };

    /**
     * The normal vector for the right face of a block.
     */
    private static final float[] RIGHT_NORMAL = new float[]{1, 0, 0};

    /**
     * The positions of the vertices of the right face of a block, relative to its anchor.
     */
    private static final float[][] RIGHT_POSITIONS = new float[][]{
            new float[]{1, 0, 0},
            new float[]{1, 0, -1},
            new float[]{1, 1, -1},
            new float[]{1, 1, 0}
    };

    /**
     * The normal vector for the top face of a block.
     */
    private static final float[] TOP_NORMAL = new float[]{0, 1, 0};

    /**
     * The positions of the vertices of the top face of a block, relative to its anchor.
     */
    private static final float[][] TOP_POSITIONS = new float[][]{
            new float[]{0, 1, 0},
            new float[]{1, 1, 0},
            new float[]{1, 1, -1},
            new float[]{0, 1, -1}
    };

    /**
     * The normal vector for the bottom face of a block.
     */
    private static final float[] BOTTOM_NORMAL = new float[]{0, -1, 0};

    /**
     * The positions of the vertices of the bottom face of a block, relative to its anchor.
     */
    private static final float[][] BOTTOM_POSITIONS = new float[][]{
            new float[]{0, 0, -1},
            new float[]{1, 0, -1},
            new float[]{1, 0, 0},
            new float[]{0, 0, 0}
    };

    private static final float[][][] PARTIAL_VERTEX_DATA = new float[][][]{
            new float[][]{Block.FRONT_POSITIONS[0], null, Block.FRONT_NORMAL},
            new float[][]{Block.FRONT_POSITIONS[1], null, Block.FRONT_NORMAL},
            new float[][]{Block.FRONT_POSITIONS[2], null, Block.FRONT_NORMAL},
            new float[][]{Block.FRONT_POSITIONS[3], null, Block.FRONT_NORMAL},
            new float[][]{Block.LEFT_POSITIONS[0], null, Block.LEFT_NORMAL},
            new float[][]{Block.LEFT_POSITIONS[1], null, Block.LEFT_NORMAL},
            new float[][]{Block.LEFT_POSITIONS[2], null, Block.LEFT_NORMAL},
            new float[][]{Block.LEFT_POSITIONS[3], null, Block.LEFT_NORMAL},
            new float[][]{Block.BACK_POSITIONS[0], null, Block.BACK_NORMAL},
            new float[][]{Block.BACK_POSITIONS[1], null, Block.BACK_NORMAL},
            new float[][]{Block.BACK_POSITIONS[2], null, Block.BACK_NORMAL},
            new float[][]{Block.BACK_POSITIONS[3], null, Block.BACK_NORMAL},
            new float[][]{Block.RIGHT_POSITIONS[0], null, Block.RIGHT_NORMAL},
            new float[][]{Block.RIGHT_POSITIONS[1], null, Block.RIGHT_NORMAL},
            new float[][]{Block.RIGHT_POSITIONS[2], null, Block.RIGHT_NORMAL},
            new float[][]{Block.RIGHT_POSITIONS[3], null, Block.RIGHT_NORMAL},
            new float[][]{Block.TOP_POSITIONS[0], null, Block.TOP_NORMAL},
            new float[][]{Block.TOP_POSITIONS[1], null, Block.TOP_NORMAL},
            new float[][]{Block.TOP_POSITIONS[2], null, Block.TOP_NORMAL},
            new float[][]{Block.TOP_POSITIONS[3], null, Block.TOP_NORMAL},
            new float[][]{Block.BOTTOM_POSITIONS[0], null, Block.BOTTOM_NORMAL},
            new float[][]{Block.BOTTOM_POSITIONS[1], null, Block.BOTTOM_NORMAL},
            new float[][]{Block.BOTTOM_POSITIONS[2], null, Block.BOTTOM_NORMAL},
            new float[][]{Block.BOTTOM_POSITIONS[3], null, Block.BOTTOM_NORMAL}
    };

    /**
     * The initial velocity of a block.
     */
    private static final double[] INITIAL_VELOCITY = new double[]{0, 0, 0};

    /**
     * Create a block.
     * @param chunk The chunk containing the block.
     * @param position The position of the block relative to the anchor point of the chunk containing it.
     */
    public Block(final Chunk chunk, final int[] position) {
        super(chunk, ArrayUtilities.toDoubleArray(position), Block.INITIAL_VELOCITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][][] getVertexData() {
        final float[][] faceColors = this.getFaceColors();
        final float[][][] vertexData = new float[Block.PARTIAL_VERTEX_DATA.length][][];
        for (int vertex = 0; vertex < Block.PARTIAL_VERTEX_DATA.length; vertex++) {
            final int face = vertex / 4;
            vertexData[vertex] = new float[][]{
                    ArrayUtilities.copy(Block.PARTIAL_VERTEX_DATA[vertex][0]),
                    ArrayUtilities.copy(faceColors[face]),
                    ArrayUtilities.copy(Block.PARTIAL_VERTEX_DATA[vertex][2])
            };
        }
        return vertexData;
    }

    /**
     * Get the colors of the faces of this block.
     *
     * The color of a face is a float array consisting of the red, green, and blue components of the face's
     * color. The colors of the faces of a block are organized into an array of arrays, with the faces' colors
     * appearing in the order front, left, back, right, top, then bottom.
     * @return The colors of the faces of this block.
     */
    protected abstract float[][] getFaceColors();
}
