package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.Collections;
import java.util.List;

/**
 * A placeholder for a block in a chunk which is not yet loaded into the world.
 */
public class UnloadedBlock extends Block {
    private static final List<float[]> FACE_COLORS = Collections.emptyList();

    private static final List<Vertex> VERTEX_DATA = Collections.emptyList();

    /**
     * Create an unloaded block.
     * @param pos See {@link WorldObject#getPos()}.
     */
    public UnloadedBlock(final Vector pos) {
        super(pos);
    }

    @Override
    public List<float[]> getFaceColors() {
        return FACE_COLORS;
    }

    @Override
    public List<Vertex> getVertices() {
        return UnloadedBlock.VERTEX_DATA;
    }
}
