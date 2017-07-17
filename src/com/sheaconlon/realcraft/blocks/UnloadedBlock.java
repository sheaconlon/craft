package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

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

    /**
     * Create an unloaded block.
     * @param pos See {@link WorldObject#getPos()}.
     */
    public UnloadedBlock(final Vector pos) {
        super(pos);
    }

    @Override
    public float[][] getFaceColors() {
        return UnloadedBlock.FACE_COLORS;
    }

    @Override
    public float[][][] getVertexData() {
        return UnloadedBlock.VERTEX_DATA;
    }

    @Override
    public double getCompressiveStrength() {
        return 10000;
    }

    @Override
    public double getMass() {
        return 0;
    }
}
