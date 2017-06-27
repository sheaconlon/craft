package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.world.Chunk;

/**
 * A block of air.
 */
public class AirBlock extends Block {
    /**
     * Create an air block.
     * @param chunk The chunk containing the air block.
     * @param position The position of the air block relative to the anchor point of the chunk containing it.
     */
    public AirBlock(final Chunk chunk, final int[] position) {
        super(chunk, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][][] getVertexData() {
        return new float[][][]{};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][] getFaceColors() {
        return new float[][]{};
    }
}

// TODO: Optimize rendering by rendering only those blocks touching air/a translucent block.
