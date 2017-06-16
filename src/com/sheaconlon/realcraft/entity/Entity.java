package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.positioning.ThreeVector;
import com.sheaconlon.realcraft.renderer.Renderable;

/**
 * Something which is not a {@link Block}.
 */
public abstract class Entity extends Physical implements Renderable {
    /**
     * Construct an entity.
     * @see Physical#Physical(Position, double, ThreeVector)
     */
    public Entity(final Position anchor, final double orientation, final ThreeVector velocity) {
        super(anchor, orientation, velocity);
    }

    /**
     * Construct an entity.
     * @see Physical#Physical(Position, double)
     */
    public Entity(final Position anchor, final double orientation) {
        super(anchor, orientation);
    }

    /**
     * Construct an entity.
     * @see Physical#Physical(Position)
     */
    public Entity(final Position anchor) {
        super(anchor);
    }

    /**
     * Construct an entity.
     * @see Physical#Physical()
     */
    public Entity() {
        super();
    }
}
