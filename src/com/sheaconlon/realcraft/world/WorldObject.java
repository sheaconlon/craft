package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.Vector;

import java.util.List;

/**
 * A world object.
 */
public abstract class WorldObject {
    private static final double FULL_REV_ANGLE = 2 * Math.PI;

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
     * Get the vertices of this world object, with positions relative to this world object's anchor point.
     * @return The vertices of this world object, with positions relative to this world object's anchor point.
     */
    public abstract List<Vertex> getVertices();

    /**
     * Get the hitboxes of this world object.
     * @return The hitboxes of this world object.
     */
    public abstract List<Hitbox> getHitboxes();

    /**
     * Act upon the world. Should be called every so often.
     *
     * The default implementation does nothing.
     */
    public void tick() {
        return;
    };

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
        this.setPos(Vector.add(this.getPos(), disp));
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
        this.setOrient(this.getOrient() + delta);
    }

    /**
     * Set the direction this world object faces. See {@link #getOrient()}.
     * @param orient The direction this world object should face. In radians.
     */
    public void setOrient(final double orient) {
        this.orient = orient % FULL_REV_ANGLE;
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
        this.setVelocity(Vector.add(this.getVelocity(), disp));
    }

    /**
     * Set the velocity of this world object.
     * @param velocity The new velocity.
     */
    public void setVelocity(final Vector velocity) {
        this.velocity = velocity;
    }
}
