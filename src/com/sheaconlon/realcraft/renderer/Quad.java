package com.sheaconlon.realcraft.renderer;

/**
 * A quadrilateral face.
 */
public class Quad extends Face {
    private final static int VERTEX_COUNT = 4;

    /**
     * Construct a quad. See {@link Face()}.
     */
    public Quad(final Vertex[] vertices, final double[] color) {
        super(vertices, color);
    }

    /**
     * See {@link Face#getVertexCount()}.
     */
    int getVertexCount() {
        return Quad.VERTEX_COUNT;
    }
}
