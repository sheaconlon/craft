package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An animal.
 */
public abstract class Animal extends Entity {
    /**
     * The minimum allowed look direction for an animal.
     */
    private static final double LOOK_DIRECTION_MINIMUM = 0.9 * -Math.PI / 2;

    /**
     * The maximum allowed look direction for an animal.
     */
    private static final double LOOK_DIRECTION_MAXIMUM = 0.9 * Math.PI / 2;

    /**
     * The angle change, in radians, which represents a full revolution.
     */
    private static final double FULL_REVOLUTION_ANGLE = 2 * Math.PI;

    /**
     * The orientation of this animal in the x-z-plane, as an angle from the positive x-axis.
     */
    private double orientation;

    /**
     * The direction this animal is looking, as an angle from the x-z-plane.
     */
    private double lookDirection;

    /**
     * Create an animal.
     * @param position See {@link WorldObject#position}.
     * @param velocity See {@link WorldObject#velocity}.
     * @param orientation See {@link #orientation}.
     * @param lookDirection See {@link #lookDirection}.
     */
    public Animal(final double[] position, final double[] velocity, final double orientation,
                  final double lookDirection) {
        super(position, velocity);
        this.orientation = orientation;
        this.lookDirection = lookDirection;
    }

    /**
     * Getter for {@link #orientation}.
     */
    public double getOrientation() {
        return this.orientation;
    }

    /**
     * Changer for {@link #orientation}.
     */
    public void changeOrientation(final double delta) {
        this.orientation += delta;
        this.orientation = this.orientation % Animal.FULL_REVOLUTION_ANGLE;
    }

    /**
     * Getter for {@link #lookDirection}.
     * @return The value of {@link #lookDirection}.
     */
    public double getLookDirection() {
        return this.lookDirection;
    }

    /**
     * Changer for {@link #lookDirection}.
     * @param delta The amount by which to change {@link #lookDirection}.
     */
    public void changeLookDirection(final double delta) {
        this.lookDirection += delta;
        this.lookDirection = Math.min(Animal.LOOK_DIRECTION_MAXIMUM, this.lookDirection);
        this.lookDirection = Math.max(Animal.LOOK_DIRECTION_MINIMUM, this.lookDirection);
    }
}
