package com.sheaconlon.realcraft.simulator.forces;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * Air resistance.
 */
public class AirResistance extends Force {
    private static final String NAME = "air resistance";
    private static final double CONSTANT = 5;

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public Vector apply(WorldObject obj, World world) {
        return Vector.multiply(
                Vector.scale(
                        Vector.multiply(
                                obj.getVelocity(),
                                obj.getVelocity()
                        ),
                        -CONSTANT
                ),
                Vector.signum(obj.getVelocity())
        );
    }
}
