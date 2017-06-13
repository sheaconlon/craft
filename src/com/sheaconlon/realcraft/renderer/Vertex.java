package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;

/**
 * A vertex of a face.
 */
public class Vertex {
    /**
     * The position of this vertex.
     */
    private final Position position;

    /**
     * The normal vector of this vertex.
     */
    private final double[] normal;

    /**
     * Construct a vertex.
     * @param position See {@link #position}.
     * @param normal See {@link #normal}.
     */
    public Vertex(final Position position, final double[] normal) {
        this.position = position;
        this.normal = normal;
    }

    /**
     * Get the position of this vertex.
     * @return See {@link #position}.
     */
    Position getPosition() {
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
