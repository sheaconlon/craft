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
     * The default value for the initial position of a physical.
     */
    private static final Position DEFAULT_INITIAL_POSITION = new Position(0, 0, 0);

    /**
     * The position of this physical, in feet.
     *
     * The components are the X, Y, and Z coordinates of the position, respectively.
     */
    private final Position position;

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
     * Construct a physical from an initial position, orientation, and velocity.
     * @param position The initial position.
     * @param orientation The initial orientation.
     * @param velocity The initial velocity.
     */
    public Physical(final Position position, final double orientation, final ThreeVector velocity) {
        this.position = position;
        this.orientation = orientation;
        this.velocity = velocity;
    }

    /**
     * Construct a physical from an initial position and orientation.
     * @param position The initial position.
     * @param orientation The initial orientation.
     */
    public Physical(final Position position, final double orientation) {
        this(position, orientation, Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Construct a physical from an initial position.
     * @param position The initial position.
     */
    public Physical(final Position position) {
        this(position, Physical.DEFAULT_INITIAL_ORIENTATION, Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Construct a physical.
     */
    public Physical() {
        this(Physical.DEFAULT_INITIAL_POSITION, Physical.DEFAULT_INITIAL_ORIENTATION,
                Physical.DEFAULT_INITIAL_VELOCITY);
    }

    /**
     * Get the bounding box of this physical.
     * @see {@link BoundingBox}.
     * @return The bounding box of this physical.
     */
    public abstract BoundingBox getBoundingBox();

    /**
     * Get the position of this physical.
     * @see {@link #position}.
     * @return The position of this physical. This object is not a copy, so changes to it will affect this
     *          physical.
     */
    public Position getPosition() {
        return this.position;
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
