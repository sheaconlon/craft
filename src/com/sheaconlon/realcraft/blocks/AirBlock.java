package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.Collections;
import java.util.List;

/**
 * A block of air.
 */
public class AirBlock extends Block {
    private static final double COMPRESSIVE_STRENGTH = 0;
    private static final double MASS = 1;
    private static final List<Hitbox> HITBOXES = Collections.emptyList();

    /**
     * Create an air block.
     * @param pos See {@link WorldObject#getPos()}.
     */
    public AirBlock(final Vector pos) {
        super(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vertex> getVertices() {
        return Collections.emptyList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][] getFaceColors() {
        return new float[][]{};
    }

    @Override
    public List<Hitbox> getHitboxes() {
        return HITBOXES;
    }
}

// TODO: Optimize rendering by rendering only those blocks touching air/a translucent block.
