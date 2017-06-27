package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An entities, any world object that is not a block.
 */
public abstract class Entity extends WorldObject {
    /**
     * Create an entities.
     * @param position See {@link WorldObject#position}.
     * @param velocity See {@link WorldObject#velocity}.
     */
    public Entity(final double[] position, final double[] velocity) {
        super(null, position, velocity);
    }
}
