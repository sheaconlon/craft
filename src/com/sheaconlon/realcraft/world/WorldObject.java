package com.sheaconlon.realcraft.world;

/**
 * A world object.
 */
public abstract class WorldObject {
    /**
     * The container of this world object.
     */
    private final Container container;

    /**
     * The position of the anchor point of this world object, relative to the position of the anchor point of
     * this world object's container.
     */
    private final double[] position;

    /**
     * The velocity of this world object.
     */
    private final double[] velocity;

    /**
     * Create a world object.
     * @param container See {@link #container}.
     * @param position See {@link #position}.
     * @param velocity See {@link #velocity}.
     */
    public WorldObject(final Container container, final double[] position, final double[] velocity) {
        this.container = container;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * Get the vertex data of this world object, with positions relative to this world object's anchor.
     *
     * The vertex data of one vertex consists of an array of 3 float arrays. The first float array consists
     * of three floats which are the x-, y-, and z-coordinates of the position of the vertex. The second float
     * array consists of three floats which are the red, green, and blue components of the color of the vertex.
     * The third float array consists of three floats which are the x-, y-, and z-coordinates of the normal
     * vector of the vertex. The vertex data of this world object consists of the array of the vertex data of all
     * its vertices, in some arbitrary, consistent order.
     * @return The vertex data of this world object, with positions relative to this world object's anchor.
     */
    public abstract float[][][] getVertexData();
}
