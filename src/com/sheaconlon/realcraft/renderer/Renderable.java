package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;

import java.util.Iterator;

/**
 * Something which can be rendered.
 */
public interface Renderable extends Iterable<Quad> {
    /**
     * Return the position of the anchor point of this renderable.
     * @return The position of the anchor point of this renderable
     */
    public Position getPosition();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Iterator<Quad> iterator();
}
