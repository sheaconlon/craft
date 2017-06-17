package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;

import java.util.Iterator;

/**
 * Something which can be rendered.
 */
public interface Renderable extends Iterable<Quad> {
    /**
     * @see Physical#anchor
     */
    public Position getAnchor();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Iterator<Quad> iterator();
}
