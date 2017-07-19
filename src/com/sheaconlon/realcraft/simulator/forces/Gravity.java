package com.sheaconlon.realcraft.simulator.forces;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * The force of gravity.
 */
public class Gravity extends Force {
    private static final String NAME = "gravity";
    private static final double ACCEL = -9.8;

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public Vector apply(WorldObject obj, World world) {
        return new Vector(0, ACCEL * obj.getMass(), 0);
    }
}
