package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.positioning.BlockPosition;

/**
 * A block, a cubical, grid-aligned object in the world.
 */
public abstract class Block implements Renderable, Physical {
    /**
     * The bounding box of a block.
     */
    private static final BoundingBox BOUNDING_BOX = new BoundingBox(1, 1, 1);

    /**
     * The normal vector for the front face of a block.
     */
    private static final double[] FRONT_NORMAL = new double[]{0, 0, 1};

    /**
     * The vertices of the front face of a block.
     */
    protected static final Vertex[] FRONT_VERTICES = new Vertex[]{
            new Vertex(new Position(0, 0, 0), Block.FRONT_NORMAL),
            new Vertex(new Position(1, 0, 0), Block.FRONT_NORMAL),
            new Vertex(new Position(1, 1, 0), Block.FRONT_NORMAL),
            new Vertex(new Position(0, 1, 0), Block.FRONT_NORMAL)
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
            new Vertex(new Position(0, 0, -1), Block.LEFT_NORMAL),
            new Vertex(new Position(0, 0, 0), Block.LEFT_NORMAL),
            new Vertex(new Position(0, 1, 0), Block.LEFT_NORMAL),
            new Vertex(new Position(0, 1, -1), Block.LEFT_NORMAL)
    };

    /**
     * The normal vector for the back face of a block.
     */
    private static final double[] BACK_NORMAL = new double[]{0, 0, -1};

    /**
     * The vertices of the back face of a block.
     */
    protected static final Vertex[] BACK_VERTICES = new Vertex[]{
            new Vertex(new Position(1, 0, -1), Block.BACK_NORMAL),
            new Vertex(new Position(0, 0, -1), Block.BACK_NORMAL),
            new Vertex(new Position(0, 1, -1), Block.BACK_NORMAL),
            new Vertex(new Position(1, 1, -1), Block.BACK_NORMAL)
    };

    /**
     * The normal vector for the right face of a block.
     */
    private static final double[] RIGHT_NORMAL = new double[]{1, 0, 0};

    /**
     * The vertices of the right face of a block.
     */
    protected static final Vertex[] RIGHT_VERTICES = new Vertex[]{
            new Vertex(new Position(1, 0, 0), Block.RIGHT_NORMAL),
            new Vertex(new Position(1, 0, -1), Block.RIGHT_NORMAL),
            new Vertex(new Position(1, 1, -1), Block.RIGHT_NORMAL),
            new Vertex(new Position(1, 1, 0), Block.RIGHT_NORMAL)
    };

    /**
     * The normal vector for the top face of a block.
     */
    private static final double[] TOP_NORMAL = new double[]{0, 1, 0};

    /**
     * The vertices of the top face of a block.
     */
    protected static final Vertex[] TOP_VERTICES = new Vertex[]{
            new Vertex(new Position(0, 1, 0), Block.TOP_NORMAL),
            new Vertex(new Position(1, 1, 0), Block.TOP_NORMAL),
            new Vertex(new Position(1, 1, -1), Block.TOP_NORMAL),
            new Vertex(new Position(0, 1, -1), Block.TOP_NORMAL)
    };

    /**
     * The normal vector for the bottom face of a block.
     */
    private static final double[] BOTTOM_NORMAL = new double[]{0, -1, 0};

    /**
     * The vertices of the bottom face of a block.
     */
    protected static final Vertex[] BOTTOM_VERTICES = new Vertex[]{
            new Vertex(new Position(0, 0, -1), Block.BOTTOM_NORMAL),
            new Vertex(new Position(1, 0, -1), Block.BOTTOM_NORMAL),
            new Vertex(new Position(1, 0, 0), Block.BOTTOM_NORMAL),
            new Vertex(new Position(0, 0, 0), Block.BOTTOM_NORMAL)
    };

    /**
     * The position of the block.
     */
    private final BlockPosition pos;

    /**
     * Construct a block.
     * @param pos See {@link #pos}.
     */
    protected Block(final BlockPosition pos) {
        this.pos = pos;
    }

    // TODO: Add @Override annotations to the rest of the code base.
    // TODO: Use @inheritDoc throughout codebase.

    /**
     * See {@link Renderable#getPosition()}.
     */
    @Override
    public Position getPosition() {
        return this.pos.toPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBox getBoundingBox() {
        return Block.BOUNDING_BOX;
    }
}
