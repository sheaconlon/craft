package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.utilities.BlockPosition;
import com.sheaconlon.realcraft.utilities.EntityPosition;

import java.util.LinkedList;

/**
 * A block of air.
 */
public class AirBlock extends Block {
    // TODO: Optimize rendering by rendering only those blocks touching air/a translucent block.
    /**
     * The quads of an air block.
     */
    private static final Iterable<Quad> QUADS = new LinkedList<>();

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
    public Iterable<Quad> getQuads() {
        return AirBlock.QUADS;
    }
}
