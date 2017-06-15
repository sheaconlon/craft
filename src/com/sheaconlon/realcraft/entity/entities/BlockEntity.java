package com.sheaconlon.realcraft.entity.entities;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.Entity;
import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.renderer.Quad;

/**
 * A block entity, a block in motion.
 */
public class BlockEntity extends Entity {
    /**
     * The initial orientation of a block entity.
     */
    private final static double INITIAL_ORIENTATION = 0;

    /**
     * The block that this block entity represents.
     */
    private final Block block;

    /**
     * Construct a block entity.
     * @param block The block that the block entity should represent.
     */
    public BlockEntity(final Block block) {
        super(block.getPosition(), BlockEntity.INITIAL_ORIENTATION);
        this.block = block;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<Quad> getQuads() {
        return this.block.getQuads();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BoundingBox getBoundingBox() {
        return this.block.getBoundingBox();
    }

    public Block getBlock() {
        return this.block;
    }
}
