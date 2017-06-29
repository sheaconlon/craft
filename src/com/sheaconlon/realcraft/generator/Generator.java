package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.utilities.PositionUtilities;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.Worker;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A generator. Responsible for procedurally generating chunks of the world and loading them into the world just in time
 * for the player to view them.
 */
public class Generator extends Worker {
    /**
     * The target tick rate of a generator, in ticks per second.
     */
    private static final int TARGET_TICK_RATE = 3;

    /**
     * The y-coordinate of the highest ground blocks.
     */
    public static final int GROUND_LEVEL = 50;

    /**
     * The number of extra chunks in each direction from the player's chunk to keep loaded.
     */
    private static final int LOAD_DISTANCE = Renderer.RENDER_DISTANCE + 1;

    /**
     * The world which this generator must generate chunks for.
     */
    private final World world;

    /**
     * Create a generator.
     *
     * @param world See {@link #world}.
     */
    public Generator(final World world) {
        this.world = world;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getTargetTickRate() {
        return Generator.TARGET_TICK_RATE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inThreadInitialize() {
        return;
    }

    /**
     * If necessary, generate any unloaded chunks near the player and load them into the world.
     */
    @Override
    public void tick() {
        final int[] playerChunkPos = PositionUtilities.toChunkPosition(this.world.getPlayer().getPosition());
        for (final int[] chunkPos : PositionUtilities.getNearbyChunkPositions(playerChunkPos, Generator.LOAD_DISTANCE)) {
            if (!world.chunkLoaded(chunkPos)) {
                world.loadChunk(chunkPos, this.generateChunk(chunkPos));
            }
        }
    }

    private Chunk generateChunk(final int[] pos) {
        final Chunk chunk = new Chunk(pos);
        final int[] blockPos = new int[3];
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (blockPos[0] = pos[0]; blockPos[0] < pos[0] + Chunk.SIZE; blockPos[0]++) {
            for (blockPos[1] = pos[1]; blockPos[1] < pos[1] + Chunk.SIZE
                    && blockPos[1] < Generator.GROUND_LEVEL; blockPos[1]++) {
                for (blockPos[2] = pos[2]; blockPos[2] < pos[2] + Chunk.SIZE; blockPos[2]++) {
                    if (random.nextBoolean()) {
                        final Block block = new DirtBlock(chunk, blockPos);
                        chunk.putBlock(blockPos, block);
                    }
                }
            }
        }
        return chunk;
    }
}
