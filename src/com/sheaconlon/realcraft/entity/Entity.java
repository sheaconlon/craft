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
     * @see Physical#Physical(Position, double, ThreeVector)
     */
    public Entity(final Position position, final double orientation, final ThreeVector velocity) {
        super(position, orientation, velocity);
    }

    /**
     * @see Physical#Physical(Position, double)
     */
    public Entity(final Position position, final double orientation) {
        super(position, orientation);
    }

    /**
     * @see Physical#Physical(Position)
     */
    public Entity(final Position position) {
        super(position);
    }

    /**
     * @see Physical#Physical()
     */
    public Entity() {
        super();
    }
}
