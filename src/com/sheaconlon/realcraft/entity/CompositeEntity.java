package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.positioning.ThreeVector;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A composite entity, an entity composed of some constituent entities.
 */
public abstract class CompositeEntity extends Entity {
    private class CompositeEntityQuadIterator implements Iterator<Quad> {
        // TODO: Make other quad iterators use parent attributes directly.

        private Iterator<Entity> constituentIterator;

        private Iterator<Quad> constituentQuadIterator;

        CompositeEntityQuadIterator() {
            this.constituentIterator = CompositeEntity.this.constituents.iterator();
            this.constituentQuadIterator = null;
        }

        public boolean hasNext() {
            if (this.constituentQuadIterator != null && this.constituentQuadIterator.hasNext()) {
                return true;
            }
            if (this.constituentIterator.hasNext()) {
                this.constituentQuadIterator = this.constituentIterator.next().iterator();
                return this.hasNext();
            }
            return false;
        }

        public Quad next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.constituentQuadIterator.next();
        }
    }

    /**
     * The constituent entities of this composite entity.
     */
    private final List<Entity> constituents;

    /**
     * The cached bounding box of this composite entity.
     */
    private BoundingBox boundingBox;

    /**
     * The cached quads of this composite entity.
     */
    private List<Quad> quads;

    /**
     * @see Physical#Physical(Position, double, ThreeVector)
     */
    protected CompositeEntity(final Position position, final double orientation, final ThreeVector velocity) {
        super(position, orientation, velocity);
        this.constituents = new LinkedList<>();
        this.boundingBox = new BoundingBox(0, 0, 0);
    }

    /**
     * @see Physical#Physical(Position, double)
     */
    protected CompositeEntity(final Position position, final double orientation) {
        super(position, orientation);
        this.constituents = new LinkedList<>();
        this.boundingBox = new BoundingBox(0, 0, 0);
    }

    /**
     * @see Physical#Physical(Position)
     */
    protected CompositeEntity(final Position position) {
        super(position);
        this.constituents = new LinkedList<>();
        this.boundingBox = new BoundingBox(0, 0, 0);
    }

    /**
     * @see Physical#Physical()
     */
    protected CompositeEntity() {
        super();
        this.constituents = new LinkedList<>();
        this.boundingBox = new BoundingBox(0, 0, 0);
    }

    /**
     * Add a constituent entity to this composite entity.
     * @param constituent The constituent entity.
     */
    protected void addConstituent(final Entity constituent) {
        this.constituents.add(constituent);
        this.updateBoundingBox(constituent);
        this.updateQuads(constituent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Quad> iterator() {
        return new CompositeEntityQuadIterator();
    }

    /**
     * Update this composite entity's cached bounding box.
     * @param newConstituent A new constituent entity that may necessitate an update of the bounding box.
     */
    private void updateBoundingBox(final Entity newConstituent) {
        final Position thisPosition = this.getAnchor();
        final BoundingBox thisBoundingBox = this.getBoundingBox();
        final Position constituentPosition = newConstituent.getAnchor();
        final BoundingBox constituentBoundingBox = newConstituent.getBoundingBox();
        final long xLength = (long)Math.max(thisBoundingBox.getXLength(),
                constituentPosition.getX() + constituentBoundingBox.getXLength());
        final long yLength = (long)Math.max(thisBoundingBox.getYLength(),
                constituentPosition.getY() + constituentBoundingBox.getYLength());
        final long zLength = (long)Math.max(thisBoundingBox.getZLength(),
                constituentPosition.getZ() + constituentBoundingBox.getZLength());
        this.boundingBox = new BoundingBox(xLength, yLength, zLength);
    }

    /**
     * Update the composite entity's cached quads.
     * @param newConstituent A new constituent entity that may necessitate an update of the quads.
     */
    private void updateQuads(final Entity newConstituent) {
        for (final Quad quad : newConstituent) {
            this.quads.add(quad);
        }
    }
}
