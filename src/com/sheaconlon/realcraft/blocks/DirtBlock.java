package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.positioning.BlockPosition;

import java.util.LinkedList;
import java.util.List;

/**
 * A block of dirt.
 */
public class DirtBlock extends Block {
    /**
     * The brown color of dirt.
     */
    private static final float[] BROWN = new float[]{(float)(132d/255d), (float)(81d/255d), (float)(10d/255d)};

    /**
     * The quads of a dirt block.
     */
    private static final Quad[] QUADS = new Quad[]{
            new Quad(Block.FRONT_VERTICES, DirtBlock.BROWN),
            new Quad(Block.LEFT_VERTICES, DirtBlock.BROWN),
            new Quad(Block.BACK_VERTICES, DirtBlock.BROWN),
            new Quad(Block.RIGHT_VERTICES, DirtBlock.BROWN),
            new Quad(Block.TOP_VERTICES, DirtBlock.BROWN),
            new Quad(Block.BOTTOM_VERTICES, DirtBlock.BROWN)
    };

    // TODO: Rewrite Javadocs for constructors since they are not inherited.
    /**
     * Construct a dirt block.
     * @param pos See {@link Block#pos}.
     */
    public DirtBlock(final BlockPosition pos) {
        super(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Quad[] getQuads() {
        return DirtBlock.QUADS;
    }
}
