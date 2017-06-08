package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Face;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.renderer.Quad;

/**
 * A block, a cubical, grid-aligned object in the world.
 */
public abstract class Block implements Renderable {
    /**
     * The normal vector for the front face of a block.
     */
    private static final double[] FRONT_NORMAL = new double[]{0, 0, 1};

    /**
     * The vertices of the front face of a block.
     */
    protected static final Vertex[] FRONT_VERTICES = new Vertex[]{
            new Vertex(new double[]{0, 0, 0}, Block.FRONT_NORMAL),
            new Vertex(new double[]{1, 0, 0}, Block.FRONT_NORMAL),
            new Vertex(new double[]{1, 1, 0}, Block.FRONT_NORMAL),
            new Vertex(new double[]{0, 1, 0}, Block.FRONT_NORMAL)
    };

    /**
     * The normal vector for the left face of a block.
     */
    private static final double[] LEFT_NORMAL = new double[]{-1, 0, 0};

    // TODO: Ensure that attributes are fully qualified throughout codebase.

    /**
     * The vertices of the left face of a block.
     */
    protected static final Vertex[] LEFT_VERTICES = new Vertex[]{
            new Vertex(new double[]{0, 0, -1}, Block.LEFT_NORMAL),
            new Vertex(new double[]{0, 0, 0}, Block.LEFT_NORMAL),
            new Vertex(new double[]{0, 1, 0}, Block.LEFT_NORMAL),
            new Vertex(new double[]{0, 1, -1}, Block.LEFT_NORMAL)
    };

    /**
     * The normal vector for the back face of a block.
     */
    private static final double[] BACK_NORMAL = new double[]{0, 0, -1};

    /**
     * The vertices of the back face of a block.
     */
    protected static final Vertex[] BACK_VERTICES = new Vertex[]{
            new Vertex(new double[]{1, 0, -1}, Block.BACK_NORMAL),
            new Vertex(new double[]{0, 0, -1}, Block.BACK_NORMAL),
            new Vertex(new double[]{0, 1, -1}, Block.BACK_NORMAL),
            new Vertex(new double[]{1, 1, -1}, Block.BACK_NORMAL)
    };

    /**
     * The normal vector for the right face of a block.
     */
    private static final double[] RIGHT_NORMAL = new double[]{1, 0, 0};

    /**
     * The vertices of the right face of a block.
     */
    protected static final Vertex[] RIGHT_VERTICES = new Vertex[]{
            new Vertex(new double[]{1, 0, 0}, Block.RIGHT_NORMAL),
            new Vertex(new double[]{1, 0, -1}, Block.RIGHT_NORMAL),
            new Vertex(new double[]{1, 1, -1}, Block.RIGHT_NORMAL),
            new Vertex(new double[]{1, 1, 0}, Block.RIGHT_NORMAL)
    };

    /**
     * The normal vector for the top face of a block.
     */
    private static final double[] TOP_NORMAL = new double[]{0, 1, 0};

    /**
     * The vertices of the top face of a block.
     */
    protected static final Vertex[] TOP_VERTICES = new Vertex[]{
            new Vertex(new double[]{0, 1, 0}, Block.TOP_NORMAL),
            new Vertex(new double[]{1, 1, 0}, Block.TOP_NORMAL),
            new Vertex(new double[]{1, 1, -1}, Block.TOP_NORMAL),
            new Vertex(new double[]{0, 1, -1}, Block.TOP_NORMAL)
    };

    /**
     * The normal vector for the bottom face of a block.
     */
    private static final double[] BOTTOM_NORMAL = new double[]{0, -1, 0};

    /**
     * The vertices of the bottom face of a block.
     */
    protected static final Vertex[] BOTTOM_VERTICES = new Vertex[]{
            new Vertex(new double[]{0, 0, -1}, Block.BOTTOM_NORMAL),
            new Vertex(new double[]{1, 0, -1}, Block.BOTTOM_NORMAL),
            new Vertex(new double[]{1, 0, 0}, Block.BOTTOM_NORMAL),
            new Vertex(new double[]{0, 0, 0}, Block.BOTTOM_NORMAL)
    };

    /**
     * The x-coordinate of the block.
     */
    private final int x;

    /**
     * The y-coordinate of the block.
     */
    private final int y;

    /**
     * The z-coordinate of the block.
     */
    private final int z;

    /**
     * Construct a block.
     * @param x See {@link #x}.
     * @param y See {@link #y}.
     * @param z See {@link #z}.
     */
    protected Block(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // TODO: Add @Override annotations to the rest of the code base.
    // TODO: Use @inheritDoc throughout codebase.

    /**
     * See {@link Renderable#getX()}.
     */
    @Override
    public int getX() {
        return this.x;
    }

    /**
     * See {@link Renderable#getY()}.
     */
    @Override
    public int getY() {
        return this.y;
    }

    /**
     * See {@link Renderable#getZ()}.
     */
    @Override
    public int getZ() {
        return this.z;
    };
}
