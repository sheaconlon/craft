package com.sheaconlon.realcraft.physics;

/**
 * A rectangular prism which fully encompasses an object.
 */
public class BoundingBox {
    /**
     * The length of this bounding box along the x-axis.
     */
    private long xLength;

    /**
     * The length of this bounding box along the y-axis.
     */
    private long yLength;

    /**
     * The length of this bounding box along the z-axis.
     */
    private long zLength;

    public BoundingBox(final long xLength, final long yLength, final long zLength) {
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    /**
     * Getter for {@link #xLength}.
     */
    public long getXLength() {
        return xLength;
    }

    /**
     * Getter for {@link #yLength}.
     */
    public long getYLength() {
        return yLength;
    }

    /**
     * Getter for {@link #zLength}.
     */
    public long getZLength() {
        return zLength;
    }
}
