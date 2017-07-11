package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An entities, any world object that is not a block.
 */
public abstract class Entity extends WorldObject {
    /**
     * Create an entities.
     * @param position See {@link WorldObject#position}.
     * @param xzOrientation See {@link WorldObject#horizontalOrientation}.
     * @param xzCrossOrientation See {@link WorldObject#verticalOrientation}.
     * @param velocity See {@link WorldObject#velocity}.
     */
    public Entity(final Vector position, final double xzOrientation, final double xzCrossOrientation,
                  final Vector velocity) {
        super(null, position, xzOrientation, xzCrossOrientation, velocity);
    }
}
