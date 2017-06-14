package com.sheaconlon.realcraft.physics;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.Entity;

/**
 * A physics effect.
 */
public abstract class Effect {
    /**
     * Return whether this effect should be applied to an entity.
     * @param entity The entity.
     * @return Whether this effect should be applied to the entity.
     */
    public abstract boolean active(final Entity entity);

    /**
     * Apply this effect to an entity.
     * @param entity The entity.
     * @param time The number of seconds to simulate the passage of.
     */
    public abstract void tick(final Entity entity, final double time);

    /**
     * Return whether this effect should be applied to a block.
     * @param block The block.
     * @return Whether this effect should be applied to the block.
     */
    public abstract boolean active(final Block block);
}
