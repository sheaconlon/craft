package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.List;
import java.util.LinkedList;

/**
 * A composite entity, an entity composed of some constitutent entity.
 */
public abstract class CompositeEntity extends Entity {
    /**
     * The constituent entity of this composite entity.
     */
    private final List<Entity> entities;

    /**
     * Construct a composite entity.
     * @param pos See {@link Entity#pos}.
     * @param xAngle See {@link Entity#xAngle}.
     * @param yAngle See {@link Entity#xAngle}.
     * @param zAngle See {@link Entity#xAngle}.
     */
    protected CompositeEntity(final Position pos, final double xAngle, final double yAngle,
                              final double zAngle) {
        super(pos, xAngle, yAngle, zAngle);
        this.entities = new LinkedList<>();
    }

    /**
     * Add a constituent entity.
     * @param e The constituent entity.
     */
    protected void addConstituent(final Entity e) {
        this.entities.add(e);
    }

    /**
     * {@inheritDoc}
     */
    public Iterable<Quad> getQuads() {
        final List<Quad> quads = new LinkedList<>();
        for (final Entity e : this.entities) {
            for (final Quad q : e.getQuads()) {
                quads.add(q);
            }
        }
        return quads;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBox getBoundingBox() {
        long xLength = 0;
        long yLength = 0;
        long zLength = 0;
        for (final Entity e : this.entities) {
            final Position ePosition = e.getPosition();
            final BoundingBox eBoundingBox = e.getBoundingBox();
            xLength = (long)Math.max(xLength, ePosition.getX() + eBoundingBox.getXLength());
            yLength = (long)Math.max(yLength, ePosition.getY() + eBoundingBox.getYLength());
            zLength = (long)Math.max(zLength, ePosition.getZ() + eBoundingBox.getZLength());
        }
        return new BoundingBox(xLength, yLength, zLength);
    }
}
