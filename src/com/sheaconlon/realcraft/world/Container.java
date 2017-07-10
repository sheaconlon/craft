package com.sheaconlon.realcraft.world;

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
    private final double[] position;

    /**
     * Create a container.
     * @param container The container of this container.
     * @param position The initial position of the anchor point of the container.
     */
    public Container(final Container container, final double[] position) {
        this.container = container;
        this.position = position;
    }

    public abstract Iterator<WorldObject> getContents();

    /**
     * Getter for {@link #position}.
     */
    public double[] getPosition() {
        return ArrayUtilities.copy(this.position);
    }
}
