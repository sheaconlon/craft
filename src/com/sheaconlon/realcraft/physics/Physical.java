package com.sheaconlon.realcraft.physics;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.positioning.ThreeVector;

/**
 * An world object which affected by physical simulation.
 */
public abstract class Physical {
    /**
     * The velocity of this physical.
     */
    private final ThreeVector velocity;

    /**
     * The position of this physical.
     */
    private final Position position;

    /**
     * Construct a physical.
     */
    public Physical(final Position position) {
        this.velocity = new ThreeVector(0, 0, 0);
        this.position = position;
    }

    /**
     * Get the bounding box of this physical.
     * @return The bounding box of this physical.
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Get the position of this physical.
     * @return The position of this physical.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Get the velocity of this physical.
     * @return The velocity of this physical.
     */
    public ThreeVector getVelocity() {
        return this.velocity;
    }
}
