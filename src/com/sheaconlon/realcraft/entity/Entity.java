package com.sheaconlon.realcraft.entity;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Renderable;

/**
 * An entity, any object in the world which is not a {@link Block}.
 */
public abstract class Entity extends Physical implements Renderable {
    /**
     * The position of the anchor point of this entity.
     */
    private Position pos;

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
     * @param pos See {@link #pos}.
     * @param xAngle See {@link #xAngle}.
     * @param yAngle See {@link #xAngle}.
     * @param zAngle See {@link #xAngle}.
     */
    protected Entity(final Position pos, final double xAngle, final double yAngle, final double zAngle) {
        this.pos = new Position(pos);
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;
    }

    /**
     * Getter for {@link #pos}.
     */
    public Position getPosition() {
        return this.pos;
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
