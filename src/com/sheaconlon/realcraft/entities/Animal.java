package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An animal.
 */
public abstract class Animal extends Entity {
    /**
     * The minimum allowed xz-cross orientation for an animal.
     */
    private static final double XZ_CROSS_ORIENTATION_MINIMUM = 0.999 * -Math.PI / 2;

    /**
     * The maximum allowed xz-cross orientation for an animal.
     */
    private static final double XZ_CROSS_ORIENTATION_MAXIMUM = 0.999 * Math.PI / 2;

    /**
     * The angle change, in radians, which represents a full revolution.
     */
    private static final double FULL_REVOLUTION_ANGLE = 2 * Math.PI;

    /**
     * Create an animal.
     * @param position See {@link WorldObject#position}.
     * @param velocity See {@link WorldObject#velocity}.
     * @param xzOrientation See {@link WorldObject#xzOrientation}.
     * @param xzCrossOrientation See {@link WorldObject#xzCrossOrientation}.
     */
    public Animal(final double[] position, final double[] velocity, final double xzOrientation,
                  final double xzCrossOrientation) {
        super(position, xzOrientation, xzCrossOrientation, velocity);
    }

    @Override
    public void changeXzOrientation(final double delta) {
        super.changeXzOrientation(delta);
        this.xzOrientation = this.xzOrientation % Animal.FULL_REVOLUTION_ANGLE;
    }

    @Override
    public void changeXzCrossOrientation(final double delta) {
        super.changeXzCrossOrientation(delta);
        this.xzCrossOrientation = Math.max(Animal.XZ_CROSS_ORIENTATION_MINIMUM, this.xzCrossOrientation);
        this.xzCrossOrientation = Math.min(Animal.XZ_CROSS_ORIENTATION_MAXIMUM, this.xzCrossOrientation);
    }
}
