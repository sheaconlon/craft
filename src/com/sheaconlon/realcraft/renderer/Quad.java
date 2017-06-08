package com.sheaconlon.realcraft.renderer;

/**
 * A quadrilateral, or quad for short.
 */
public class Quad {
    /**
     * The vertices of this quad, in the order they occur going counterclockwise around the front face of this
     * quad.
     */
    private final Vertex[] vertices;

    /**
     * The color of this quad, in RGB format.
     */
    private final double[] color;

    /**
     * Construct a quad.
     * @param vertices See {@link #vertices}.
     */
    public Quad(final Vertex[] vertices, final double[] color) {
        if (vertices.length != 4) {
            throw new RuntimeException("incorrect number of vertices supplied to Quad constructor");
        }
        this.vertices = vertices;
        this.color = color;
    }

    /**
     * Get the vertices of this quad.
     * @return See {@link #vertices}.
     */
    Vertex[] getVertices() {
        return this.vertices;
    }

    /**
     * Get the color of this quad.
     * @return See {@link #color}.
     */
    double[] getColor() {
        return this.color;
    }
}
