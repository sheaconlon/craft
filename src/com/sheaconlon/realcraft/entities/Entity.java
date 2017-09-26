package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An entity, a world object that is not a block.
 */
public abstract class Entity extends WorldObject {
    // ##### PRIVATE STATIC #####
    private static int currID = 0;

    // ##### PRIVATE FINAL #####
    private final int id;

    // ##### CONSTRUCTORS #####
    /**
     * Create an entity.
     * @param pos See {@link WorldObject#getPos()}.
     * @param orient See {@link WorldObject#getOrient()}.
     * @param velocity See {@link WorldObject#getVelocity()}.
     */
    public Entity(final Vector pos, final double orient, final Vector velocity) {
        super(pos, orient, velocity);
        this.id = currID++;
    }

    // ##### OVERRIDES OF OBJECT #####
    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Entity)) {
            return false;
        }
        final Entity e = (Entity)o;
        return this.id == e.id;
    }
}
