package com.sheaconlon.realcraft.physics;

import com.sheaconlon.realcraft.positioning.Position;

/**
 * An world object which affected by physical simulation.
 */
public abstract class Physical {
    /**
     * Get the bounding box of this physical.
     * @return The bounding box of this physical.
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Get the position of this physical.
     * @return The position of this physical.
     */
    public abstract Position getPosition();
}
