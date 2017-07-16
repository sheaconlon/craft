package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * A physical force.
 */
public abstract class Force {
    /**
     * Should return the name of this force.
     */
    @Override
    public abstract String toString();

    /**
     * Get the force exerted on some object.
     * @param obj The object.
     * @return The force exerted on {@code obj}. Each component in Newtons.
     */
    public abstract Vector apply(final WorldObject obj);
}
