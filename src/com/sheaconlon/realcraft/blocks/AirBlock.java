package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Face;
import com.sheaconlon.realcraft.renderer.Quad;

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
     * @param x See {@link Block#x}.
     * @param y See {@link Block#y}.
     * @param z See {@link Block#z}.
     */
    public AirBlock(final int x, final int y, final int z) {
        super(x, y, z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Quad> getQuads() {
        return AirBlock.QUADS;
    }
}
