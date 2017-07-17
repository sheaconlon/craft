package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.Vector;

/**
 * A world object.
 */
public abstract class WorldObject {
    private Vector pos;
    private double orient;
    private Vector velocity;

    /**
     * Create a world object.
     * @param pos The position of the anchor point of the world object.
     * @param orient The direction the world object faces in the xz-plane, as an angle from the positive x-axis
     *               towards the negative z-axis. In radians.
     * @param velocity The velocity of the world object. Each component in blocks per second.
     */
    public WorldObject(final Vector pos, final double orient, final Vector velocity) {
        this.pos = pos;
        this.orient = orient;
        this.velocity = velocity;
    }

    /**
     * Get the vertex data of this world object, with positions relative to this world object's anchor point.
     *
     * The vertex data of one vertex consists of an array of 3 float arrays. The first float array consists
     * of three floats which are the position of the vertex. The second float array consists of three floats
     * which are the RGB color of the vertex. The third float array consists of three floats which are the normal
     * vector of the vertex. The vertex data of this world object consists of the array of the vertex data of all
     * its vertices, in some arbitrary, consistent order.
     * @return The vertex data of this world object, with positions relative to this world object's anchor point.
     */
    public abstract float[][][] getVertexData();

    /**
     * Get the mass of this world object.
     * @return The mass of this world object. In kilograms.
     */
    public abstract double getMass();

    /**
     * Get the compressive strength of this world object.
     * @return The compressive strength of this world object. In Newtons per meter.
     */
    public abstract double getCompressiveStrength();

    /**
     * Get the hitboxes of this world object.
     * @return The hitboxes of this world object.
     */
    public abstract Hitbox[] getHitboxes();

    /**
     * Act upon the world. Should be called every so often.
     */
    public abstract void tick(final World world);

    /**
     * @return The position of the anchor point of this world object.
     */
    public Vector getPos() {
        return this.pos;
    }

    /**
     * Change this world object's position. See {@link #getPos()}.
     * @param disp The displacement to add to the position.
     */
    public void changePos(final Vector disp) {
        this.pos = Vector.add(this.pos, disp);
    }

    /**
     * Set this world object's position. See {@link #getPos()}.
     * @param pos The new position.
     */
    public void setPos(final Vector pos) {
        this.pos = pos;
    }

    /**
     * Get the direction this world object faces in the xz-plane.
     * @return The direction this world object faces in the xz-plane, as an angle from the positive x-axis
     *         towards the negative z-axis. In radians.
     */
    public double getOrient() {
        return this.orient;
    }

    /**
     * Change the direction this world object faces. See {@link #getOrient()}.
     * @param delta The change in the direction this world object faces. In radians.
     */
    public void changeOrient(final double delta) {
        this.orient += delta;
    }

    /**
     * Set the direction this world object faces. See {@link #getOrient()}.
     * @param orient The direction this world object should face. In radians.
     */
    public void setOrient(final double orient) {
        this.orient = orient;
    }

    /**
     * @return The velocity of this world object.
     */
    public Vector getVelocity() {
        return this.velocity;
    }

    /**
     * Change the velocity of this world object.
     * @param disp The displacement to add to the velocity.
     */
    public void changeVelocity(final Vector disp) {
        this.velocity = Vector.add(this.velocity, disp);
    }

    /**
     * Set the velocity of this world object.
     * @param velocity The new velocity.
     */
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity;
    }
}
