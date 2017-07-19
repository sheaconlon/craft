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
    private static final double VERT_ORIENT_MIN = 0.999 * -Math.PI / 2;

    /**
     * The maximum allowed vertical orientation for an animal.
     */
    private static final double VERT_ORIENT_MAX = 0.999 * Math.PI / 2;

    private double vertOrient;

    /**
     * Create an animal.
     * @param position See {@link WorldObject#getPos()}.
     * @param velocity See {@link WorldObject#getVelocity()}.
     * @param orient See {@link WorldObject#getOrient()}.
     * @param vertOrient See {@link #getVertOrient()}.
     */
    public Animal(final Vector position, final Vector velocity, final double orient, final double vertOrient) {
        super(position, orient, velocity);
        this.vertOrient = vertOrient;
    }

    /**
     * Get this animal's look direction.
     *
     * As an angle from horizontal. Towards the positive y-axis is positive.
     * @return This animal's look direction. In radians.
     */
    public double getVertOrient() {
        return this.vertOrient;
    }

    /**
     * Change this animal's look direction. See {@link #getVertOrient()}.
     * @param delta The amount to change it by. In radians.
     */
    public void changeVertOrient(final double delta) {
        this.setVertOrient(this.getVertOrient() + delta);
    }

    /**
     * Set this animal's look direction. See {@link #getVertOrient()}.
     * @param vertOrient The new look direction. In radians.
     */
    public void setVertOrient(final double vertOrient) {
        this.vertOrient = vertOrient;
        this.vertOrient = Math.max(Animal.VERT_ORIENT_MIN, this.vertOrient);
        this.vertOrient = Math.min(Animal.VERT_ORIENT_MAX, this.vertOrient);
    }
}
