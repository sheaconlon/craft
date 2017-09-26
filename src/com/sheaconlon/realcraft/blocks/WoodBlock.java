package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A wood block.
 */
public class WoodBlock extends Block {
    private static final float[] WOOD_BROWN = new float[]{96f/255f, 51f/255f, 31f/255f};
    private static final List<float[]> FACE_COLORS = Collections.unmodifiableList(Stream.of(
            WOOD_BROWN,
            WOOD_BROWN,
            WOOD_BROWN,
            WOOD_BROWN,
            WOOD_BROWN,
            WOOD_BROWN
    ).collect(Collectors.toList()));

    /**
     * Create a wood block.
     * @param pos Its position.
     */
    public WoodBlock(Vector pos) {
        super(pos);
    }

    @Override
    protected List<float[]> getFaceColors() {
        return FACE_COLORS;
    }
}
