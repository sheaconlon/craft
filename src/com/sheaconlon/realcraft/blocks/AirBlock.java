package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.positioning.BlockPosition;

import java.util.LinkedList;

/**
 * A block of air.
 */
public class AirBlock extends Block {
    // TODO: Optimize rendering by rendering only those blocks touching air/a translucent block.
    /**
     * The quads of an air block.
     */
    private static final Quad[] QUADS = new Quad[]{};

    /**
     * Construct an air block.
     * @param pos See {@link Block#pos}.
     */
    public AirBlock(final BlockPosition pos) {
        super(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quad[] getQuads() {
        return AirBlock.QUADS;
    }
}
