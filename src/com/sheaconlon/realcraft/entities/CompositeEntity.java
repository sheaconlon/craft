package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.renderer.Quad;

import java.util.List;
import java.util.LinkedList;

/**
 * A composite entity, an entity composed of some constitutent entities.
 */
public abstract class CompositeEntity extends Entity {
    /**
     * The constituent entities of this composite entity.
     */
    private final List<Entity> entities;

    /**
     * Construct a composite entity.
     * @param x See {@link Entity#x}.
     * @param y See {@link Entity#y}.
     * @param z See {@link Entity#z}.
     * @param xAngle See {@link Entity#xAngle}.
     * @param yAngle See {@link Entity#xAngle}.
     * @param zAngle See {@link Entity#xAngle}.
     */
    protected CompositeEntity(final double x, final double y, final double z,
                    final double xAngle, final double yAngle, final double zAngle) {
        super(x, y, z, xAngle, yAngle, zAngle);
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
}
