package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An entity, any world object that is not a block.
 */
public abstract class Entity extends WorldObject {
    /**
     * Create an entity.
     * @param pos See {@link WorldObject#getPos()}.
     * @param orient See {@link WorldObject#getOrient()}.
     * @param velocity See {@link WorldObject#getVelocity()}.
     */
    public Entity(final Vector pos, final double orient, final Vector velocity) {
        super(pos, orient, velocity);
    }
}
