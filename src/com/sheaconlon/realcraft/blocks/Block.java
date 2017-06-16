package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Renderable;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A block, a cubical, grid-aligned object in the world.
 */
public abstract class Block extends Physical implements Renderable {
    private class BlockQuadIterator implements Iterator<Quad> {
        private int quadIndex;

        BlockQuadIterator() {
            this.quadIndex = 0;
        }

        public boolean hasNext() {
            return this.quadIndex < Block.this.getQuads().length;
        }

        public Quad next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final Quad next = Block.this.getQuads()[this.quadIndex];
            final Quad absoluteNext = next.makeAbsolute(Block.this.getPosition());
            this.quadIndex++;
            return absoluteNext;
        }
    }

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
     * Construct a block.
     * @see Physical#Physical(Position)
     */
    protected Block(final BlockPosition anchor) {
        super(anchor.toPosition());
    }

    // TODO: Add @Override annotations to the rest of the code base.
    // TODO: Use @inheritDoc throughout codebase.

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBox getBoundingBox() {
        return Block.BOUNDING_BOX;
    }

    /**
     * Get the quads of this block.
     * @return The quads of this block.
     */
    protected abstract Quad[] getQuads();

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Quad> iterator() {
        return new BlockQuadIterator();
    }
}
