package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Face;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.LinkedList;
import java.util.List;

/**
 * A block of dirt.
 */
public class DirtBlock extends Block {
    /**
     * The brown color of dirt.
     */
    private static final double[] BROWN = new double[]{132/255, 81/255, 10/255};

    /**
     * The faces of a dirt block.
     */
    private static final List<Face> FACES = new LinkedList<>();
    static {
            DirtBlock.FACES.add(new Quad(Block.FRONT_VERTICES, DirtBlock.BROWN));
            DirtBlock.FACES.add(new Quad(Block.LEFT_VERTICES, DirtBlock.BROWN));
            DirtBlock.FACES.add(new Quad(Block.BACK_VERTICES, DirtBlock.BROWN));
            DirtBlock.FACES.add(new Quad(Block.RIGHT_VERTICES, DirtBlock.BROWN));
            DirtBlock.FACES.add(new Quad(Block.TOP_VERTICES, DirtBlock.BROWN));
            DirtBlock.FACES.add(new Quad(Block.BOTTOM_VERTICES, DirtBlock.BROWN));
    }

    // TODO: Rewrite Javadocs for constructors since they are not inherited.
    /**
     * Construct a dirt block.
     * @param x See {@link Block#x}.
     * @param y See {@link Block#y}.
     * @param z See {@link Block#z}.
     */
    public DirtBlock(final int x, final int y, final int z) {
        super(x, y, z);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Face> getFaces() {
        return DirtBlock.FACES;
    }
}
