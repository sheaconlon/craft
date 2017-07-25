package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A block of air.
 */
public class DirtBlock extends Block {
    private static final double COMPRESSIVE_STRENGTH = 10;
    private static final double MASS = 100;
    private static final float[] BROWN = new float[]{0.518f, 0.318f, 0.039f};
    private static final List<float[]> FACE_COLORS = Collections.unmodifiableList(Stream.of(
            DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN,
            DirtBlock.BROWN, DirtBlock.BROWN, DirtBlock.BROWN
    ).collect(Collectors.toList()));

    /**
     * Create a dirt block.
     * @param pos See {@link WorldObject#getPos()}
     */
    public DirtBlock(final Vector pos) {
        super(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<float[]> getFaceColors() {
        return FACE_COLORS;
    }
}