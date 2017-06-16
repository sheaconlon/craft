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
     * Get the absolute x-coordinate of this integer position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute x-coordinate of this integer position, if it is relative to the reference position.
     */
    public long getXAbsolute(final IntPosition reference) {
        return reference.getX() + this.getX();
    }

    /**
     * Get the absolute y-coordinate of this integer position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute y-coordinate of this integer position, if it is relative to the reference position.
     */
    public long getYAbsolute(final IntPosition reference) {
        return reference.getY() + this.getY();
    }

    /**
     * Get the absolute z-coordinate of this integer position, if it is relative to some reference position.
     * @param reference The reference position.
     * @return The absolute z-coordinate of this integer position, if it is relative to the reference position.
     */
    public long getZAbsolute(final IntPosition reference) {
        return reference.getZ() + this.getZ();
    }

    /**
     * Get the x-coordinate of this integer position relative to some reference position.
     * @param reference The reference position.
     * @return The x-coordinate of this integer position relative to the reference position.
     */
    public long getXRelative(final IntPosition reference) {
        return this.getX() - reference.getX();
    }

    /**
     * Get the y-coordinate of this integer position relative to some reference position.
     * @param reference The reference position.
     * @return The y-coordinate of this integer position relative to the reference position.
     */
    public long getYRelative(final IntPosition reference) {
        return this.getY() - reference.getY();
    }

    /**
     * Get the z-coordinate of this integer position relative to some reference position.
     * @param reference The reference position.
     * @return The z-coordinate of this integer position relative to the reference position.
     */
    public long getZRelative(final IntPosition reference) {
        return this.getZ() - reference.getZ();
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

    public boolean isOrigin() {
        // TODO: Make the world "wrap around".
        return this.getX() == 0 && this.getY() == 0 && this.getZ() == 0;
    }
}
