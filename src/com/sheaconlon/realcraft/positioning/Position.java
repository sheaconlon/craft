package com.sheaconlon.realcraft.positioning;

import java.util.Arrays;

/**
 * A position in 3D space.
 */
public abstract class Position {
    /**
     * The x-coordinate of this position.
     */
    private double x;

    /**
     * The y-coordinate of this position.
     */
    private double y;

    /**
     * The z-coordinate of this position.
     */
    private double z;

    /**
     * Construct a position.
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     * @param z The z-coordinate of the position.
     */
    protected Position(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the x-coordinate of this position.
     * @return The x-coordinate of this position.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Get the y-coordinate of this position.
     * @return The y-coordinate of this position.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Get the z-coordinate of this position.
     * @return The z-coordinate of this position.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Set the x-coordinate of this position.
     * @param x The new x-coordinate.
     */
    public void setX(final double x) {
        this.x = x;
    }

    /**
     * Set the y-coordinate of this position.
     * @param y The new y-coordinate.
     */
    public void setY(final double y) {
        this.y = y;
    }

    /**
     * Set the z-coordinate of this position.
     * @param z The new z-coordinate.
     */
    public void setZ(final double z) {
        this.z = z;
    }

    /**
     * Change the x-coordinate of this position by some amount.
     * @param delta The amount to change the x-coordinate of this position by.
     */
    public void changeX(final double delta) {
        this.setX(this.getX() + delta);
    }

    /**
     * Change the y-coordinate of this position by some amount.
     * @param delta The amount to change the y-coordinate of this position by.
     */
    public void changeY(final double delta) {
        this.setY(this.getY() + delta);
    }

    /**
     * Change the z-coordinate of this position by some amount.
     * @param delta The amount to change the z-coordinate of this position by.
     */
    public void changeZ(final double delta) {
        this.setZ(this.getZ() + delta);
    }

    /**
     * Floor a coordinate.
     * @param coordinate The coordinate.
     * @return The largest integer that is still less than or equal to {@code coordinate}.
     */
    static long floorCoordinate(final double coordinate) {
        return (long)Math.floor(coordinate);
    }

    /**
     * Get the hash code of this position.
     * @return The hash code of this position.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new double[]{this.getX(), this.getY(), this.getZ()});
    }

    /**
     * Return whether this position equals some other position.
     * @param other The other position.
     * @return Whether this position equals the other position.
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Position)) {
            return false;
        }
        final Position otherPosition = (Position)other;
        return this.getX() == otherPosition.getX()
                && this.getY() == otherPosition.getY()
                && this.getZ() == otherPosition.getZ();
    }
}
