package com.sheaconlon.realcraft.positioning;

import java.util.Arrays;

/**
 * A position on the 3D integer lattice.
 */
public abstract class IntPosition {
    /**
     * The x-coordinate of this integer position.
     */
    private long x;

    /**
     * The y-coordinate of this integer position.
     */
    private long y;

    /**
     * The z-coordinate of this integer position.
     */
    private long z;

    /**
     * Construct an integer position.
     * @param x The x-coordinate of the integer position.
     * @param y The y-coordinate of the integer position.
     * @param z The z-coordinate of the integer position.
     */
    protected IntPosition(final long x, final long y, final long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get the x-coordinate of this integer position.
     * @return The x-coordinate of this integer position.
     */
    public long getX() {
        return this.x;
    }

    /**
     * Get the y-coordinate of this integer position.
     * @return The y-coordinate of this integer position.
     */
    public long getY() {
        return this.y;
    }

    /**
     * Get the z-coordinate of this integer position.
     * @return The z-coordinate of this integer position.
     */
    public long getZ() {
        return this.z;
    }

    /**
     * Set the x-coordinate of this integer position.
     * @param x The new x-coordinate.
     */
    public void setX(final long x) {
        this.x = x;
    }

    /**
     * Set the y-coordinate of this integer position.
     * @param y The new y-coordinate.
     */
    public void setY(final long y) {
        this.y = y;
    }

    /**
     * Set the z-coordinate of this integer position.
     * @param z The new z-coordinate.
     */
    public void setZ(final long z) {
        this.z = z;
    }

    /**
     * Change the x-coordinate of this integer position by some amount.
     * @param delta The amount to change the x-coordinate of this integer position by.
     */
    public void changeX(final long delta) {
        this.setX(this.getX() + delta);
    }

    /**
     * Change the y-coordinate of this integer position by some amount.
     * @param delta The amount to change the y-coordinate of this integer position by.
     */
    public void changeY(final long delta) {
        this.setY(this.getY() + delta);
    }

    /**
     * Change the z-coordinate of this integer position by some amount.
     * @param delta The amount to change the z-coordinate of this integer position by.
     */
    public void changeZ(final long delta) {
        this.setZ(this.getZ() + delta);
    }

    /**
     * Get the hash code of this position.
     * @return The hash code of this position.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(new long[]{this.getX(), this.getY(), this.getZ()});
    }

    /**
     * Return whether this position equals some other position.
     * @param other The other position.
     * @return Whether this position equals the other position.
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof IntPosition)) {
            return false;
        }
        final IntPosition otherPosition = (IntPosition)other;
        return this.getX() == otherPosition.getX()
                && this.getY() == otherPosition.getY()
                && this.getZ() == otherPosition.getZ();
    }


}
