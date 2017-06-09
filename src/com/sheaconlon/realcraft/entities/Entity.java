package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.renderer.Renderable;

/**
 * An entity, any object in the world which is not a {@link Block}.
 */
public abstract class Entity implements Renderable {
    /**
     * The x-coordinate of the position of the anchor point of this entity.
     */
    private double x;

    /**
     * The y-coordinate of the position of the anchor point of this entity.
     */
    private double y;

    /**
     * The z-coordinate of the position of the anchor point of this entity.
     */
    private double z;

    /**
     * The component counterclockwise from the positive x-axis of the orientation of this entity.
     */
    private double xAngle;

    /**
     * The component counterclockwise from the positive y-axis of the orientation of this entity.
     */
    private double yAngle;

    /**
     * The component counterclockwise from the positive z-axis of the orientation of this entity.
     */
    private double zAngle;

    /**
     * Construct an entity with a given initial position and orientation.
     * @param x See {@link #x}.
     * @param y See {@link #y}.
     * @param z See {@link #z}.
     * @param xAngle See {@link #xAngle}.
     * @param yAngle See {@link #xAngle}.
     * @param zAngle See {@link #xAngle}.
     */
    protected Entity(final double x, final double y, final double z,
                  final double xAngle, final double yAngle, final double zAngle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;
    }

    /**
     * Getter for {@link #x}.
     */
    public double getX() {
        return this.x;
    }

    /**
     * Getter for {@link #y}.
     */
    public double getY() {
        return this.y;
    }

    /**
     * Getter for {@link #z}.
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Getter for {@link #xAngle}.
     */
    public double getXAngle() {
        return this.xAngle;
    }

    /**
     * Getter for {@link #yAngle}.
     */
    public double getYAngle() {
        return this.yAngle;
    }

    /**
     * Getter for {@link #zAngle}.
     */
    public double getZAngle() {
        return this.zAngle;
    }
}
