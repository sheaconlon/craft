package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * An animal.
 */
public abstract class Animal extends Entity {
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
    }
}
