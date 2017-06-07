package com.sheaconlon.realcraft.renderer;

/**
 * A face of a renderable.
 */
public abstract class Face {
    /**
     * The vertices of this face, in the order they occur going counterclockwise around the front face of this
     * face.
     */
    private final Vertex[] vertices;

    /**
     * The color of this face, in RGB format.
     */
    private final double[] color;

    /**
     * Construct a quad.
     * @param vertices See {@link #vertices}.
     */
    public Face(final Vertex[] vertices, final double[] color) {
        if (vertices.length != this.getVertexCount()) {
            throw new RuntimeException("incorrect number of vertices supplied to Face constructor");
        }
        this.vertices = vertices;
        this.color = color;
    }

    /**
     * Get this face's vertex count.
     * @return This face's vertex count.
     */
    abstract int getVertexCount();

    /**
     * Get the vertices of this face.
     * @return See {@link #vertices}.
     */
    Vertex[] getVertices() {
        return this.vertices;
    }

    /**
     * Get the color of this face.
     * @return See {@link #color}.
     */
    double[] getColor() {
        return this.color;
    }
}
