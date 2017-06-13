package com.sheaconlon.realcraft.positioning;

import java.util.Arrays;

/**
 * A 3-vector.
 */
public class ThreeVector {
    /**
     * The x-coordinate of this vector.
     */
    private double x;

    /**
     * The y-coordinate of this vector.
     */
    private double y;

    /**
     * The z-coordinate of this vector.
     */
    private double z;

    /**
     * Construct a vector.
     * @param x See {@link #x}.
     * @param y See {@link #y}.
     * @param z See {@link #z}.
     */
    public ThreeVector(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Construct a vector which is the same as some other vector.
     * @param vec The other vector.
     */
    public ThreeVector(final ThreeVector vec) {
        this(vec.getX(), vec.getY(), vec.getZ());
    }

    /**
     * Get the x-coordinate of this vector.
     * @return The x-coordinate of this vector.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the y-coordinate of this vector.
     * @return The y-coordinate of this vector.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the z-coordinate of this vector.
     * @return The z-coordinate of this vector.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Set the x-coordinate of this vector.
     * @param x The new x-coordinate.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Set the y-coordinate of this vector.
     * @param y The new y-coordinate.
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Set the z-coordinate of this vector.
     * @param z The new z-coordinate.
     */
    public void setZ(final double z) {
        this.z = z;
    }

    /**
     * Change the x-coordinate of this vector by some amount.
     * @param delta The amount to change the x-coordinate of this vector by.
     */
    public void changeX(final double delta) {
        this.setX(this.getX() + delta);
    }

    /**
     * Change the y-coordinate of this vector by some amount.
     * @param delta The amount to change the y-coordinate of this vector by.
     */
    public void changeY(final double delta) {
        this.setY(this.getY() + delta);
    }

    /**
     * Change the z-coordinate of this vector by some amount.
     * @param delta The amount to change the z-coordinate of this vector by.
     */
    public void changeZ(final double delta) {
        this.setZ(this.getZ() + delta);
    }

    /**
     * Get the hash code of this vector.
     * @return The hash code of this vector.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{this.getX(), this.getY(), this.getZ()});
    }

    /**
     * Return whether this vector equals some other vector.
     * @param other The other vector.
     * @return Whether this vector equals the other vector.
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof ThreeVector)) {
            return false;
        }
        final ThreeVector otherVector = (ThreeVector)other;
        return this.getX() == otherVector.getX()
                && this.getY() == otherVector.getY()
                && this.getZ() == otherVector.getZ();
    }
}
