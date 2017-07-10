package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A world object.
 */
public abstract class WorldObject {
    /**
     * The number of rewinds that a world object is capable of.
     */
    private static final int REWIND_COUNT = 1000;

    /**
     * The container of this world object.
     */
    private final Container container;

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
     * The {@link #REWIND_COUNT} previous configurations that this world object has been in. A configuration
     * consists of the x component of the position, the y component of the position, the z component of the
     * position, the xz orientation, and the xz-cross orientation.
     */
    private final Deque<double[]> prevConfigs;

    /**
     * Create a world object.
     * @param container See {@link #container}.
     * @param position See {@link #position}.
     * @param velocity See {@link #velocity}.
     */
    public WorldObject(final Container container, final Vector position, final double horizontalOrientation,
                       final double verticalOrientation, final Vector velocity) {
        this.container = container;
        this.position = position;
        this.horizontalOrientation = horizontalOrientation;
        this.verticalOrientation = verticalOrientation;
        this.velocity = velocity;
        this.prevConfigs = new ConcurrentLinkedDeque<>();
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
        this.recordConfig();
        this.position = Vector.add(this.position, delta);
    }

    /**
     * Get the value of {@link #horizontalOrientation}.
     * @return The value of {@link #horizontalOrientation}.
     */
    public double getXzOrientation() {
        return this.horizontalOrientation;
    }

    /**
     * Change the value of {@link #horizontalOrientation}.
     * @param delta The amount by which to change the value of {@link #horizontalOrientation}.
     */
    public void changeHorizontalOrientation(final double delta) {
        this.recordConfig();
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
        this.recordConfig();
        this.verticalOrientation += delta;
    }

    /**
     * Rewind this world object's position, xz orientation, and xz-cross orientation to their previous values.
     */
    public void rewind() {
        if (this.prevConfigs.size() == 0) {
            return;
        }
        final double[] config = this.prevConfigs.removeLast();
        this.position = new Vector(config[0], config[1], config[2]);
        this.horizontalOrientation = config[3];
        this.verticalOrientation = config[4];
    }

    /**
     * Record the current configuration of this world object in {@link #prevConfigs}.
     */
    private void recordConfig() {
        if (this.prevConfigs.size() == WorldObject.REWIND_COUNT) {
            this.prevConfigs.removeFirst();
        }
        this.prevConfigs.addLast(new double[]{
                this.position.getX(),
                this.position.getY(),
                this.position.getZ(),
                this.horizontalOrientation,
                this.verticalOrientation
        });
    }
}
