package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An entity, any world object that is not a block.
 */
public abstract class Entity extends WorldObject {
    /**
     * Create an entity.
     * @param position See {@link WorldObject#position}.
     * @param velocity See {@link WorldObject#velocity}.
     */
    public Entity(final double[] position, final double[] velocity) {
        super(null, position, velocity);
    }
}
