package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A leaf block.
 */
public class LeafBlock extends Block {
    private static final float[] LEAF_GREEN = new float[]{99f/255f, 204f/255f, 105f/255f};
    private static final List<float[]> FACE_COLORS = Collections.unmodifiableList(Stream.of(
            LEAF_GREEN,
            LEAF_GREEN,
            LEAF_GREEN,
            LEAF_GREEN,
            LEAF_GREEN,
            LEAF_GREEN
    ).collect(Collectors.toList()));

    /**
     * Create a leaf block.
     * @param pos Its position.
     */
    public LeafBlock(Vector pos) {
        super(pos);
    }

    @Override
    protected List<float[]> getFaceColors() {
        return FACE_COLORS;
    }
}
