package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;

/**
 * A block of air.
 */
public class DirtBlock extends Block {
    /**
     * The brown color of a dirt block.
     */
    private static final float[] BROWN = new float[]{0.518f, 0.318f, 0.039f};

    /**
     * Create a dirt block.
     * @param chunk The chunk containing the dirt block.
     * @param position The position of the dirt block relative to the anchor point of the chunk containing it.
     */
    public DirtBlock(final Chunk chunk, final Vector position) {
        super(chunk, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][] getFaceColors() {
        return new float[][]{DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN,
                DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN};
    }
}