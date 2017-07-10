package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Iterator;

/**
 * A container of world objects.
 */
public abstract class Container {
    /**
     * The container of this container.
     */
    private final Container container;

    /**
     * The position of the anchor point of this container, relative to the position of the anchor point of this
     * container's container.
     */
    private final Vector position;

    /**
     * Create a container.
     * @param container The container of this container.
     * @param position The initial position of the anchor point of the container.
     */
    public Container(final Container container, final Vector position) {
        this.container = container;
        this.position = position;
    }

    public abstract Iterator<WorldObject> getContents();

    /**
     * Getter for {@link #position}.
     */
    public Vector getPosition() {
        return this.position;
    }
}
