package com.sheaconlon.realcraft.utilities;

import java.util.*;

/**
 * A 3-vector.
 *
 * A vector is immutable, provided that its type argument is immutable.
 */
public class Vector {
    /**
     * The vector whose components are all zero.
     */
    public static final Vector ZERO_VECTOR = new Vector(0, 0, 0);

    /**
     * The x-component of this vector.
     */
    private final double x;

    /**
     * The y-component of this vector.
     */
    private final double y;

    /**
     * The z-component of this vector.
     */
    private final double z;

    /**
     * Create a vector.
     * @param x A value for {@link #x}.
     * @param y A value for {@link #y}.
     * @param z A value for {@link #z}.
     */
    public Vector(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return The value of {@link #x}.
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return The value of {@link #y}.
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return The value of {@link #z}.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Get the maximum component of this vector.
     * @return The maximum component of this vector.
     */
    public double max() {
        return Math.max(Math.max(this.x, this.y), this.z);
    }

    /**
     * Get the squared magnitude of this vector.
     * @return The squared magnitude of this vector.
     */
    public double sqMag() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Get the magnitude of this vector.
     * @return The magnitude of this vector.
     */
    public double mag() {
        return Math.sqrt(this.sqMag());
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.x) + Double.hashCode(this.y) + Double.hashCode(this.z);
    }

    @Override
    /**
     * Checks for exact equality of the x-, y-, and z-components of the two vectors. Due to the possibility of rounding errors,
     * this check is probably only meaningful for integer vectors.
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Vector)) {
            return false;
        }
        final Vector otherVector = (Vector)other;
        return this.x == otherVector.x && this.y == otherVector.y && this.z == otherVector.z;
    }

    @Override
    public String toString() {
        return (new Formatter()).format("(%f, %f, %f)", this.x, this.y, this.z).toString();
    }

    /**
     * Add two vectors.
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise sum of {@code a} and {@code b}.
     */
    public static Vector add(final Vector a, final Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Subtract two vectors.
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise difference of {@code a} - {@code b}.
     */
    public static Vector subtract(final Vector a, final Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Multiply two vectors.
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise product of {@code a} and {@code b}.
     */
    public static Vector multiply(final Vector a, final Vector b) {
        return new Vector(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    /**
     * Scale a vector.
     * @param v The vector to scale.
     * @param c The factor to scale {@code v} by.
     * @return {@code v} scaled by {@code c}.
     */
    public static Vector scale(final Vector v, final double c) {
        return new Vector(c * v.x, c * v.y, c * v.z);
    }

    /**
     * Round a vector.
     * @param v The vector.
     * @return A vector whose *-component is the largest integer that is no larger than {@code v}'s *-component.
     */
    public static Vector round(final Vector v) {
        return new Vector(Math.floor(v.x), Math.floor(v.y), Math.floor(v.z));
    }

    /**
     * Rotate a vector horizontally.
     *
     * "Horizontal" means within the xz-plane. A rotation of {@code Math.PI / 2} radians would rotate the positive x-axis to
     * overlap with the negative z-axis.
     * @param v The vector.
     * @param theta The angle to rotate {@code v} through. In radians.
     * @return {@code v} rotated horizontally by {@code theta}.
     */
    public static Vector rotateHorizontal(final Vector v, final double theta) {
        return new Vector(
                v.x * Math.cos(theta) + v.z * Math.sin(theta),
                v.y,
                v.x * Math.sin(theta) + v.z * Math.cos(theta)
        );
    }

    /**
     * Rotate a vector vertically.
     *
     * "Vertical" means perpendicular to the xz-plane. A rotation of {@code Math.PI / 2} radians would rotate any vector in the
     * xz-plane to overlap the positive y-axis.
     * @param v The vector.
     * @param theta The angle to rotate {@code v} through. In radians.
     * @return {@code v} rotated vertically by {@code theta}.
     */
    public static Vector rotateVertical(final Vector v, final double theta) {
        return new Vector(
                v.x * Math.cos(theta),
                v.y * Math.cos(theta) + (new Vector(v.x, 0, v.z).mag()) * Math.sin(theta),
                v.z * Math.cos(theta)
        );
    }

    /**
     * Get the vectors which are "nearby" some vector and at integer displacements from it.
     * @param v The vector.
     * @param distance The maximum distance at which a vector will be considered nearby {@code v}.
     * @return The vectors which are "nearby" {@code v} and at integer displacements from it.
     */
    public static Iterable<Vector> getNearby(final Vector v, final double distance) {
        final Set<Vector> nearby = new HashSet<>();
        for (double radius = 0; radius <= distance; radius++) {
            for (double x = v.getX() - radius; x <= v.getX() + radius; x++) {
                for (double y = v.getY() - radius; y <= v.getY() + radius; y++) {
                    for (double z = v.getZ() - radius; z <= v.getZ() + radius; z++) {
                        nearby.add(new Vector(x, y, z));
                    }
                }
            }
        }
        return nearby;
    }
}
