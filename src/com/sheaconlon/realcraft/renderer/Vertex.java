package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

/**
 * A vertex. Part of a quadrilateral.
 */
public class Vertex {
    public static final int POSITION_SIZE = 3;
    public static final int COLOR_SIZE = 3;
    public static final int NORMAL_SIZE = 3;

    private static final Map<Vertex, Integer> indexMap = new HashMap<>();
    private static int currentIndex = 0;

    private final float[] data;

    /**
     * Create a vertex.
     * @param position The position of the vertex.
     * @param color The color of the vertex.
     * @param normal The normal vector of the vertex.
     */
    public Vertex(final float[] position, final float[] color, final float[] normal) {
        if (position.length != POSITION_SIZE) {
            throw new IllegalArgumentException("position not of correct length");
        }
        if (color.length != COLOR_SIZE) {
            throw new IllegalArgumentException("color not of correct length");
        }
        if (normal.length != NORMAL_SIZE) {
            throw new IllegalArgumentException("normal not of correct length");
        }
        this.data = new float[position.length + color.length + normal.length];
        int i = 0;
        for (final float val : position) {
            this.data[i] = val;
            i++;
        }
        for (final float val : color) {
            this.data[i] = val;
            i++;
        }
        for (final float val : normal) {
            this.data[i] = val;
            i++;
        }
        if (!indexMap.containsKey(this)) {
            indexMap.put(this, currentIndex);
            currentIndex++;
        }
    }

    /**
     * Get the vertex data of this vertex.
     * @return The data describing this vertex. Consists of {@link #POSITION_SIZE} values describing its
     * position, {@link #COLOR_SIZE} values describing its color, and {@link #NORMAL_SIZE} values describing its
     * normal vector.
     */
    public float[] data() {
        return Arrays.copyOf(this.data, this.data.length);
    }

    /**
     * Get the index of this vertex.
     * @return An index identifying this vertex. Any other vertex for which {@link #equals(Object)} returns true
     * has the same index. Any other vertex for which {@link #equals(Object)} return false has a different index.
     */
    public int index() {
        return indexMap.get(this);
    }

    /**
     * Translate this vertex.
     * @param disp The displacement to apply.
     * @return A new vertex which is like this vertex, with its position translated by {@code disp}.
     */
    public Vertex translate(final Vector disp) {
        final float[] newData = Arrays.copyOf(this.data, this.data.length);
        final double[] dispArr = disp.toArray();
        for (int i = 0; i < POSITION_SIZE; i++) {
            newData[i] += dispArr[i];
        }
        return new Vertex(newData);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    /**
     * Vertices are equal if and only if their vertex data is equal.
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Vertex)) {
            return false;
        }
        final Vertex otherVertex = (Vertex)other;
        return Arrays.equals(this.data(), otherVertex.data());
    }

    private Vertex(final float[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }
}
