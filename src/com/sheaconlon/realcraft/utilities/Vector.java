package com.sheaconlon.realcraft.utilities;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A 3-vector.
 *
 * A vector is immutable, provided that its type argument is immutable.
 */
public class Vector {
    // ##### PUBLIC STATIC FINAL #####
    public static final List<Vector> UNIT_CUBE_VERTICES = Collections.unmodifiableList(
            Stream.of(
                new Vector(0, 0, 0),
                new Vector(1, 0, 0),
                new Vector(1, 0, 1),
                new Vector(0, 0, 1),
                new Vector(0, 1, 0),
                new Vector(1, 1, 0),
                new Vector(1, 1, 1),
                new Vector(0, 1, 1)
            ).collect(
                    Collectors.toList()
            )
    );
    public static final Vector ZERO = Vector.uniform(0);

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
     *
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
     * Create a vector.
     *
     * @param arr An array of the vector's components.
     */
    public Vector(final double[] arr) {
        this(arr[0], arr[1], arr[2]);
    }

    /**
     * @return The value of {@link #x}.
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return The x-component of this vector, rounded down to the nearest integer.
     */
    public int getXInt() {
        return (int) Math.floor(this.x);
    }

    /**
     * @return The value of {@link #y}.
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return The y-component of this vector, rounded down to the nearest integer.
     */
    public int getYInt() {
        return (int) Math.floor(this.y);
    }

    /**
     * @return The value of {@link #z}.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * @return An array of this vector's components.
     */
    public double[] toArray() {
        return new double[]{this.getX(), this.getY(), this.getZ()};
    }

    /**
     * @return The z-component of this vector, rounded down to the nearest integer.
     */
    public int getZInt() {
        return (int) Math.floor(this.z);
    }

    /**
     * Get the maximum component of this vector.
     *
     * @return The maximum component of this vector.
     */
    public double max() {
        return Math.max(Math.max(this.x, this.y), this.z);
    }

    /**
     * Get the squared magnitude of this vector.
     *
     * @return The squared magnitude of this vector.
     */
    public double sqMag() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    /**
     * Get the magnitude of this vector.
     *
     * @return The magnitude of this vector.
     */
    public double mag() {
        return Math.sqrt(this.sqMag());
    }

    @Override
    public int hashCode() {
        return Double.hashCode(this.x) ^ Double.hashCode(this.y) ^ Double.hashCode(this.z);
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
        final Vector otherVector = (Vector) other;
        return this.x == otherVector.x && this.y == otherVector.y && this.z == otherVector.z;
    }

    @Override
    public String toString() {
        return (new Formatter()).format("(%f, %f, %f)", this.x, this.y, this.z).toString();
    }

    /**
     * @return Whether this vector equals the zero vector.
     */
    public boolean isZero() {
        return this.equals(ZERO);
    }

    /**
     * Return whether all of this vector's components are integers.
     * @return Whether all of this vector's components are integers.
     */
    public boolean isInt() {
        return this.getX() == this.getXInt()
                && this.getY() == this.getYInt()
                && this.getZ() == this.getZInt();
    }

    /**
     * Return the sum of this vector's components.
     *
     * @return The sum of this vector's components.
     */
    public double sum() {
        return this.getX() + this.getY() + this.getZ();
    }

    /**
     * Add two vectors.
     *
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise sum of {@code a} and {@code b}.
     */
    public static Vector add(final Vector a, final Vector b) {
        return new Vector(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    /**
     * Subtract two vectors.
     *
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise difference of {@code a} - {@code b}.
     */
    public static Vector subtract(final Vector a, final Vector b) {
        return new Vector(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    /**
     * Multiply two vectors.
     *
     * @param a A vector.
     * @param b A vector.
     * @return The element-wise product of {@code a} and {@code b}.
     */
    public static Vector multiply(final Vector a, final Vector b) {
        return new Vector(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    /**
     * Scale a vector.
     *
     * @param v The vector to scale.
     * @param c The factor to scale {@code v} by.
     * @return {@code v} scaled by {@code c}.
     */
    public static Vector scale(final Vector v, final double c) {
        return new Vector(c * v.x, c * v.y, c * v.z);
    }

    /**
     * Round a vector.
     *
     * @param v The vector.
     * @return A vector whose *-component is the largest integer that is no larger than {@code v}'s *-component.
     */
    public static Vector round(final Vector v) {
        return new Vector(Math.floor(v.x), Math.floor(v.y), Math.floor(v.z));
    }

    /**
     * Rotate a vector horizontally.
     * <p>
     * "Horizontal" means about the y-axis. A positive rotation goes in the same direction as the angle from the
     * positive x-axis to the negative z-axis.
     *
     * @param v     The vector.
     * @param theta The angle to rotate through. In radians.
     * @return A new vector that is {@code v} rotated horizontally by {@code theta}.
     */
    public static Vector rotateHorizontal(final Vector v, double theta) {
        // Formula derived from http://www.wolframalpha.com/input/?i=rotate+30+degrees+around+y-axis.
        return new Vector(
                v.x * Math.cos(theta) + v.z * Math.sin(theta),
                v.y,
                -v.x * Math.sin(theta) + v.z * Math.cos(theta)
        );
    }

    /**
     * Rotate a vector vertically.
     * <p>
     * "Vertical" means about the z-axis. Positive rotations are in the same direction as the angle from the positive x-axis
     * to the positive y-axis.
     *
     * @param v     The vector.
     * @param theta The angle to rotate through. In radians.
     * @return {@code v} rotated vertically by {@code theta}.
     */
    public static Vector rotateVertical(final Vector v, final double theta) {
        return new Vector(
                v.x * Math.cos(theta) - v.y * Math.sin(theta),
                v.y * Math.cos(theta) + v.x * Math.sin(theta),
                v.z
        );
    }

    /**
     * Get the vectors which are "nearby" some vector and at integer displacements from it.
     *
     * @param v        The vector.
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

    /**
     * Get the vectors which bound some set of vectors.
     *
     * @param vectors A set of vectors. Must contain at least 1 vector. Cannot contain null.
     * @return The minimal coordinate values over {@code vectors} and the maximal coordinate values over
     * {@code vectors}.
     */
    public static Vector[] bounds(final Vector[] vectors) {
        if (vectors.length == 0) {
            throw new IllegalArgumentException("Cannot get the bounds of a size-0 set of vectors.");
        }
        double xMin = Double.POSITIVE_INFINITY, yMin = Double.POSITIVE_INFINITY, zMin = Double.POSITIVE_INFINITY,
                xMax = Double.NEGATIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY, zMax = Double.NEGATIVE_INFINITY;
        for (final Vector v : vectors) {
            if (v == null) {
                throw new IllegalArgumentException("Cannot get the bounds of a set of vectors containing null.");
            }
            xMin = Math.min(xMin, v.getX());
            yMin = Math.min(yMin, v.getY());
            zMin = Math.min(zMin, v.getZ());
            xMax = Math.max(xMax, v.getX());
            yMax = Math.max(yMax, v.getY());
            zMax = Math.max(zMax, v.getZ());
        }
        return new Vector[]{
                new Vector(xMin, yMin, zMin),
                new Vector(xMax, yMax, zMax)
        };
    }

    /**
     * Create a vector of Normal deviates.
     *
     * @param mu    The mean of the Normal distribution to draw from.
     * @param sigma The standard deviation of the Normal distribution to draw from.
     * @return A vector of Normal deviates.
     */
    public static Vector normal(final double mu, final double sigma) {
        return new Vector(
                ThreadLocalRandom.current().nextGaussian() * sigma + mu,
                ThreadLocalRandom.current().nextGaussian() * sigma + mu,
                ThreadLocalRandom.current().nextGaussian() * sigma + mu
        );
    }

    /**
     * @return The componentwise signum (see {@link Math#signum(double)}) of {@code v}.
     */
    public static Vector signum(final Vector v) {
        return new Vector(
                Math.signum(v.getX()),
                Math.signum(v.getY()),
                Math.signum(v.getZ())
        );
    }

    /**
     * Return whether two vectors are about equal.
     *
     * @param a       A vector.
     * @param b       A vector.
     * @param epsilon The error that is to be allowed.
     * @return Whether the corresponding components of {@code a} and {@code b} differ by no more than
     * {@code epsilon}.
     */
    public static boolean aboutEquals(final Vector a, final Vector b, final double epsilon) {
        final Vector diff = Vector.subtract(a, b);
        final Vector absDiff = Vector.abs(diff);
        final double maxAbsDiff = absDiff.max();
        return maxAbsDiff <= epsilon;
    }

    /**
     * Apply a function to a vector component-wise.
     *
     * @param v The vector.
     * @param f The function.
     * @return A new vector which is the component-wise application of {@code f} on {@code v}.
     */
    public static Vector apply(final Vector v, final UnaryOperator<Double> f) {
        return new Vector(
                f.apply(v.getX()),
                f.apply(v.getY()),
                f.apply(v.getZ())
        );
    }

    /**
     * Get the component-wise absolute value of a vector.
     *
     * @param v The vector.
     * @return A new vector which is the component-wise absolute value of {@code v}.
     */
    public static Vector abs(final Vector v) {
        return apply(v, Math::abs);
    }

    private static class VectorsBetween implements Iterable<Vector> {
        private final Vector lo;
        private final Vector hi;

        VectorsBetween(final Vector lo, final Vector hi) {
            this.lo = lo;
            this.hi = hi;
        }

        private class VectorsBetweenIterator implements Iterator<Vector> {
            private Vector next;

            VectorsBetweenIterator() {
                this.next = VectorsBetween.this.lo;
            }

            @Override
            public boolean hasNext() {
                return this.next != null;
            }

            @Override
            public Vector next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Vector result = this.next;
                this.next = Vector.changeX(this.next, 1);
                if (this.next.getX() > VectorsBetween.this.hi.getX()) {
                    this.next = new Vector(
                            VectorsBetween.this.lo.getX(),
                            this.next.getY() + 1,
                            this.next.getZ()
                    );
                }
                if (this.next.getY() > VectorsBetween.this.hi.getY()) {
                    this.next = new Vector(
                            VectorsBetween.this.lo.getX(),
                            VectorsBetween.this.lo.getY(),
                            this.next.getZ() + 1
                    );
                }
                if (this.next.getZ() > VectorsBetween.this.hi.getZ()) {
                    this.next = null;
                }
                return result;
            }
        }

        @Override
        public Iterator<Vector> iterator() {
            return new VectorsBetweenIterator();
        }
    }

    /**
     * Return the integer vectors in the rectangular prism between some vectors.
     *
     * Includes the corners.
     * @param lo The low-x, low-y, low-z corner of the prism.
     * @param hi The high-x, high-y, high-z corner of the prism.
     * @return The integer vectors in the rectangular prism with corners {@code lo} and {@code hi}.
     */
    public static Iterable<Vector> between(final Vector lo, final Vector hi) {
        return new VectorsBetween(Vector.ceil(lo), Vector.round(hi));
    }

    /**
     * Return a vector with some x-coordinate.
     * @param v The original vector.
     * @param x The x-coordinate.
     * @return A copy of {@code v} with its x-coordinate changed to {@code x}.
     */
    public static Vector setX(final Vector v, final double x) {
        return new Vector(x, v.getY(), v.getZ());
    }

    /**
     * Return a vector with a changed x-coordinate.
     * @param v The original vector.
     * @param deltaX The amount to change the x-coordinate by.
     * @return A copy of {@code v} with its x-coordinate changed by {@code deltaX}.
     */
    public static Vector changeX(final Vector v, final double deltaX) {
        return setX(v, v.getX() + deltaX);
    }

    /**
     * Return a vector with some y-coordinate.
     * @param v The original vector.
     * @param y The y-coordinate.
     * @return A copy of {@code v} with its y-coordinate changed to {@code y}.
     */
    public static Vector setY(final Vector v, final double y) {
        return new Vector(v.getX(), y, v.getZ());
    }

    /**
     * Return a vector with a changed y-coordinate.
     * @param v The original vector.
     * @param deltaY The amount to change the y-coordinate by.
     * @return A copy of {@code v} with its y-coordinate changed by {@code deltaY}.
     */
    public static Vector changeY(final Vector v, final double deltaY) {
        return setY(v, v.getY() + deltaY);
    }

    /**
     * Return a vector with components all equal to some value.
     * @param val The value.
     * @return A vector with components all equal to {@code val}.
     */
    public static Vector uniform(final double val) {
        return new Vector(val, val, val);
    }

    /**
     * Return the element-wise ceiling of a vector.
     *
     * The ceiling is performed as in {@link Math#ceil(double)}.
     * @param v The vector.
     * @return The element-wise ceiling of {@code v}.
     */
    public static Vector ceil(final Vector v) {
        return Vector.apply(v, Math::ceil);
    }

    private static class VectorsAround implements Iterable<Vector> {
        private class VectorsAroundIterator implements Iterator<Vector> {
            private final Iterator<Vector> candidates;
            private boolean mayHaveNext;
            private Vector next;

            VectorsAroundIterator() {
                final Vector disp = Vector.uniform(VectorsAround.this.radius);
                final Vector lo = Vector.ceil(Vector.subtract(VectorsAround.this.center, disp));
                final Vector hi = Vector.round(Vector.add(VectorsAround.this.center, disp));
                this.candidates = new VectorsBetween(lo, hi).iterator();
                this.mayHaveNext= true;
                this.next = null;
            }

            @Override
            public boolean hasNext() {
                if (this.next == null) {
                    if (this.mayHaveNext) {
                        this.refill();
                        return this.hasNext();
                    } else {
                        return false;
                    }
                }
                return true;
            }

            @Override
            public Vector next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final Vector result = this.next;
                this.next = null;
                return result;
            }

            private void refill() {
                while (this.candidates.hasNext()) {
                    final Vector candidate = this.candidates.next();
                    if (this.shouldInclude(candidate)) {
                        this.next = candidate;
                        return;
                    }
                }
                this.next = null;
                this.mayHaveNext = false;
            }

            private boolean shouldInclude(final Vector candidate) {
                return Vector.subtract(candidate, VectorsAround.this.center).sqMag() <= Math.pow(VectorsAround.this.radius, 2);
            }
        }

        private final Vector center;
        private final double radius;

        VectorsAround(final Vector center, final double radius) {
            this.center = center;
            this.radius = radius;
        }

        @Override
        public Iterator<Vector> iterator() {
            return new VectorsAroundIterator();
        }
    }

    /**
     * Return the integer vectors in the sphere around some vector.
     *
     * Includes vectors on the surface of the sphere.
     * @param center The center of the sphere.
     * @param radius The radius of the sphere.
     * @return The integer vectors within the sphere with center {@code center} and radius {@code radius}.
     */
    public static Iterable<Vector> around(final Vector center, final double radius) {
        return new VectorsAround(center, radius);
    }
}
