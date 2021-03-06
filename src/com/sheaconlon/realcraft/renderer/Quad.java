package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A quadrilateral. Part of a mesh. Composed of vertices.
 */
public class Quad {
    private List<Vertex> vertices;

    /**
     * Create a quad.
     * @param vertices The vertices of the quad.
     */
    public Quad(final Collection<? extends Vertex> vertices) {
        this.vertices = new LinkedList<>(vertices);
    }

    /**
     * Create a quad.
     * @param vertices The vertices of the quad.
     */
    public Quad(final Vertex... vertices) {
        this(Arrays.asList(vertices));
    }

    /**
     * Get the vertices of this quad.
     * @return The vertices of this quad.
     */
    public List<Vertex> vertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    /**
     * Translate this quad.
     * @param disp The displacement to apply.
     * @return A new quad which is like this quad, with all positions translated  by {@code disp}.
     */
    public Quad translate(final Vector disp) {
        final List<Vertex> newVertices = this.vertices.stream()
                .map(vertex -> vertex.translate(disp))
                .collect(Collectors.toList());
        return new Quad(newVertices);
    }

    /**
     * Get this quad with some color.
     * @param color The color.
     * @return A new quad which is like this quad, with the color {@code color}.
     */
    public Quad withColor(final float[] color) {
        return new Quad(this.vertices.stream().map(v -> v.withColor(color)).collect(Collectors.toList()));
    }
}
