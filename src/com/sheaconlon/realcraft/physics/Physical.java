package com.sheaconlon.realcraft.physics;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.positioning.ThreeVector;

/**
 * Something which is affected by physical simulation.
 */
public abstract class Physical {
    /**
     * The default value for the initial velocity of a physical.
     */
    private static final ThreeVector DEFAULT_INITIAL_VELOCITY = new ThreeVector(0, 0, 0);

    /**
     * The default value for the initial orientation of a physical.
     */
    private static final double DEFAULT_INITIAL_ORIENTATION = 0;

    /**
     * The default anchor of a physical.
     */
    private static final Position DEFAULT_ANCHOR = Position.ZERO;

    /**
     * The anchor of this physical is the position whose x, y, and z coordinates are the minimal x, y, and z
     * coordinates, respectively, of any position within this physical.
     */
    private final Position anchor;

    /**
     * The orientation of this physical, in radians.
     *
     * The orientation of a physical is its angle of rotation about the line which is parallel to the y-axis and
     * passes through the physical's bounding box's center.
     */
    private final double orientation;

    /**
     * The velocity of this physical, in feet per second.
     *
     * The components are the components of the velocity parallel to the X, Y, and Z axes, respectively.
     */
    private final ThreeVector velocity;

    /**
     * Construct a physical from an anchor, orientation, and velocity.
     * @param anchor The anchor. See {@link #anchor}.
     * @param orientation The initial orientation.
     * @param velocity The initial velocity.
     */
    public Physical(final Position anchor, final double orientation, final ThreeVector velocity) {
        this.anchor = anchor;
        this.orientation = orientation;
        this.velocity = velocity;
    }

    /**
     * Construct a physical from an anchor and orientation.
     * @param anchor The anchor. See {@link #anchor}.
     * @param orientation The initial orientation.
     */
    public Physical(final Position anchor, final double orientation) {
        this(anchor, orientation, Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Construct a physical from an anchor.
     * @param anchor The anchor. See {@link #anchor}.
     */
    public Physical(final Position anchor) {
        this(anchor, Physical.DEFAULT_INITIAL_ORIENTATION, Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Construct a physical.
     */
    public Physical() {
        this(new Position(Physical.DEFAULT_ANCHOR), Physical.DEFAULT_INITIAL_ORIENTATION,
                Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Get the bounding box of this physical.
     * @see {@link BoundingBox}.
     * @return The bounding box of this physical.
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Getter for {@link #anchor}.
     */
    public Position getAnchor() {
        return this.anchor;
    }

    /**
     * Get the orientation of this physical.
     * @see {@link #orientation}.
     * @return The orientation of this physical.
     */
    public double getOrientation() {
        return this.orientation;
    }

    /**
     * Get the velocity of this physical.
     * @see {@link #velocity}
     * @return The velocity of this physical. This object is not a copy, so changes to it will affect this
     *          physical.
     */
    public ThreeVector getVelocity() {
        return this.velocity;
    }
}
