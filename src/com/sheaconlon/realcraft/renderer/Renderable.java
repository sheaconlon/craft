package com.sheaconlon.realcraft.renderer;

/**
 * Something which can be rendered.
 */
public interface Renderable {
    /**
     * Return the x-coordinate of the anchor point of this renderable.
     * @return The x-coordinate of the anchor point of this renderable
     */
    public int getX();

    /**
     * Return the y-coordinate of the anchor point of this renderable.
     * @return The y-coordinate of the anchor point of this renderable
     */
    public int getY();

    /**
     * Return the z-coordinate of the anchor point of this renderable.
     * @return The z-coordinate of the anchor point of this renderable
     */
    public int getZ();

    /**
     * Get the quads that make up this renderable.
     * @return The quads that make up this renderable.
     */
    public Quad[] getQuads();
}
