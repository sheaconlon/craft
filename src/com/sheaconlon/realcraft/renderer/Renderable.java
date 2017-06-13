package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;

/**
 * Something which can be rendered.
 */
public interface Renderable {
    /**
     * Return the position of the anchor point of this renderable.
     * @return The position of the anchor point of this renderable
     */
    public Position getPosition();

    /**
     * Get the quads that make up this renderable.
     * @return The quads that make up this renderable.
     */
    public Iterable<Quad> getQuads();
}
