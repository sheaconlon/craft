package com.sheaconlon.realcraft.physics;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.Entity;

/**
 * A physics effect.
 */
public abstract class Effect {
    /**
     * Apply this effect to an entity.
     * @param entity The entity.
     * @param time The number of seconds to simulate the passage of.
     */
    public abstract void tick(final Entity entity, final double time);

    /**
     * Apply this effect to a block.
     * @param block The block.
     * @param time The number of seconds to simulate the passage of.
     */
    public abstract void tick(final Block block, final double time);
}
