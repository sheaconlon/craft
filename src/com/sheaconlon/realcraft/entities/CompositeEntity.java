package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.utilities.EntityPosition;

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
     * @param pos See {@link Entity#pos}.
     * @param xAngle See {@link Entity#xAngle}.
     * @param yAngle See {@link Entity#xAngle}.
     * @param zAngle See {@link Entity#xAngle}.
     */
    protected CompositeEntity(final EntityPosition pos, final double xAngle, final double yAngle,
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
}
