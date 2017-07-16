package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An animal.
 */
public abstract class Animal extends Entity {
    /**
     * The minimum allowed vertical orientation for an animal.
     */
    private static final double VERTICAL_ORIENTATION_MINIMUM = 0.999 * -Math.PI / 2;

    /**
     * The maximum allowed vertical orientation for an animal.
     */
    private static final double VERTICAL_ORIENTATION_MAXIMUM = 0.999 * Math.PI / 2;

    /**
     * The angle change, in radians, which represents a full revolution.
     */
    private static final double FULL_REVOLUTION_ANGLE = 2 * Math.PI;

    /**
     * Create an animal.
     * @param position See {@link WorldObject#position}.
     * @param velocity See {@link WorldObject#velocity}.
     * @param xzOrientation See {@link WorldObject#orient}.
     * @param xzCrossOrientation See {@link WorldObject#vertOrient}.
     */
    public Animal(final Vector position, final Vector velocity, final double xzOrientation,
                  final double xzCrossOrientation) {
        super(position, xzOrientation, xzCrossOrientation, velocity);
    }

    @Override
    public void changeHorizontalOrientation(final double delta) {
        super.changeHorizontalOrientation(delta);
        this.orient = this.orient % Animal.FULL_REVOLUTION_ANGLE;
    }

    @Override
    public void changeVerticalOrientation(final double delta) {
        super.changeVerticalOrientation(delta);
        this.vertOrient = Math.max(Animal.VERTICAL_ORIENTATION_MINIMUM, this.vertOrient);
        this.vertOrient = Math.min(Animal.VERTICAL_ORIENTATION_MAXIMUM, this.vertOrient);
    }
}
