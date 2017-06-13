package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.utilities.EntityPosition;

/**
 * A vertex of a face.
 */
public class Vertex {
    /**
     * The position of this vertex.
     */
    private final EntityPosition position;

    /**
     * The normal vector of this vertex.
     */
    private final double[] normal;

    /**
     * Construct a vertex.
     * @param position See {@link #position}.
     * @param normal See {@link #normal}.
     */
    public Vertex(final EntityPosition position, final double[] normal) {
        this.position = position;
        this.normal = normal;
    }

    /**
     * Get the position of this vertex.
     * @return See {@link #position}.
     */
    EntityPosition getPosition() {
        return this.position;
    }

    /**
     * Get the normal vector of this vertex.
     * @return See {@link #normal}.
     */
    double[] getNormal() {
        return this.normal;
    }
}
