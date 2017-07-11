package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;

/**
 * A placeholder for a block in a chunk which is not yet loaded into the world.
 */
public class UnloadedBlock extends Block {
    /**
     * See {@link Block#getFaceColors()}. There are no rendered faces.
     */
    private static final float[][] FACE_COLORS = new float[][]{};

    /**
     * See {@link Block#getVertexData()}. There are no rendered vertices.
     */
    private static final float[][][] VERTEX_DATA = new float[][][]{};

    public UnloadedBlock(final Vector pos) {
        super(null, pos);
    }

    @Override
    public float[][] getFaceColors() {
        return UnloadedBlock.FACE_COLORS;
    }

    @Override
    public float[][][] getVertexData() {
        return UnloadedBlock.VERTEX_DATA;
    }
}
