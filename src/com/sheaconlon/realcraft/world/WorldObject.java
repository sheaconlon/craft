package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A world object.
 */
public abstract class WorldObject {
    /**
     * The position of the anchor point of this world object, relative to the position of the anchor point of
     * this world object's container.
     */
    private Vector position;

    /**
     * The angle between the "forward" direction of this world object and the positive x-axis, within the
     * xz-plane, where towards negative z is positive and towards positive z is negative.
     */
    protected double horizontalOrientation;

    /**
     * The angle between the "forward" direction of this world object and the xz-plane, where upward is positive
     * and downward is negative.
     */
    protected double verticalOrientation;

    /**
     * The velocity of this world object.
     */
    private final Vector velocity;

    /**
     * Create a world object.
     * @param position See {@link #position}.
     * @param velocity See {@link #velocity}.
     */
    public WorldObject(final Container container, final Vector position, final double horizontalOrientation,
                       final double verticalOrientation, final Vector velocity) {
        this.position = position;
        this.horizontalOrientation = horizontalOrientation;
        this.verticalOrientation = verticalOrientation;
        this.velocity = velocity;
    }

    /**
     * Get the vertex data of this world object, with positions relative to this world object's anchor.
     *
     * The vertex data of one vertex consists of an array of 3 float arrays. The first float array consists
     * of three floats which are the x-, y-, and z-coordinates of the position of the vertex. The second float
     * array consists of three floats which are the red, green, and blue components of the color of the vertex.
     * The third float array consists of three floats which are the x-, y-, and z-coordinates of the normal
     * vector of the vertex. The vertex data of this world object consists of the array of the vertex data of all
     * its vertices, in some arbitrary, consistent order.
     * @return The vertex data of this world object, with positions relative to this world object's anchor.
     */
    public abstract float[][][] getVertexData();

    /**
     * Get the dimensions of this world object's hit box.
     *
     * An array of side lengths along the x-, y-, and z-axes, in blocks. The hit box is a rectangular prism extending
     * from the world object's anchor point towards the positive x, y, and z directions. Null if the world object
     * has no hit box.
     * @return The dimensions of this world object's hit box, or null if it has none.
     */
    public abstract double[] getHitBoxDims();

    /**
     * Getter for {@link #position}.
     */
    public Vector getPosition() {
        return this.position;
    }

    /**
     * Changer for {@link #position}.
     */
    public void changePosition(final Vector delta) {
        this.position = Vector.add(this.position, delta);
    }

    /**
     * Get the value of {@link #horizontalOrientation}.
     * @return The value of {@link #horizontalOrientation}.
     */
    public double getHorizontalOrientation() {
        return this.horizontalOrientation;
    }

    /**
     * Change the value of {@link #horizontalOrientation}.
     * @param delta The amount by which to change the value of {@link #horizontalOrientation}.
     */
    public void changeHorizontalOrientation(final double delta) {
        this.horizontalOrientation += delta;
    }

    /**
     * Get the value of {@link #verticalOrientation}.
     * @return The value of {@link #verticalOrientation}.
     */
    public double getVerticalOrientation() {
        return this.verticalOrientation;
    }

    /**
     * Change the value of {@link #verticalOrientation}.
     * @param delta The amount by which to change the value of {@link #verticalOrientation}.
     */
    public void changeVerticalOrientation(final double delta) {
        this.verticalOrientation += delta;
    }
}
