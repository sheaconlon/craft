package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * A block of air.
 */
public class DirtBlock extends Block {
    private static final double COMPRESSIVE_STRENGTH = 10;
    private static final double MASS = 100;

    /**
     * The brown color of a dirt block.
     */
    private static final float[] BROWN = new float[]{0.518f, 0.318f, 0.039f};

    /**
     * Create a dirt block.
     * @param pos See {@link WorldObject#getPos()}
     */
    public DirtBlock(final Vector pos) {
        super(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][] getFaceColors() {
        return new float[][]{DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN,
                DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN};
    }

    @Override
    public double getCompressiveStrength() {
        return COMPRESSIVE_STRENGTH;
    }

    @Override
    public double getMass() {
        return MASS;
    }
}