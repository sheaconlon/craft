package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;

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
    private final float[] color;

    /**
     * Construct a quad.
     * @param vertices See {@link #vertices}.
     */
    public Quad(final Vertex[] vertices, final float[] color) {
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
    float[] getColor() {
        return this.color;
    }

    /**
     * Return a new quad which is this quad with positions made absolute with respect to a reference position.
     * @param reference The reference position.
     * @return A new quad which is this quad with positions made absolute with respect to the reference position.
     */
    public Quad makeAbsolute(final Position reference) {
        final Vertex[] oldVertices = this.getVertices();
        final float[] oldColor = this.getColor();
        final Vertex[] newVertices = new Vertex[oldVertices.length];
        for (int i = 0; i < oldVertices.length; i++) {
            final Position oldPosition = oldVertices[i].getPosition();
            final float[] oldNormal = oldVertices[i].getNormal();
            final Position newPosition = new Position(oldPosition.getXAbsolute(reference),
                    oldPosition.getYAbsolute(reference), oldPosition.getZAbsolute(reference));
            newVertices[i] = new Vertex(newPosition, oldNormal);
        }
        return new Quad(newVertices, oldColor);
    }
}
