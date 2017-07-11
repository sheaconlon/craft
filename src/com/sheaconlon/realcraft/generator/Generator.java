package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.utilities.Vector;
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
     * A generator's return value for {@link #getInitialMinInterval()}.
     */
    private static final long INITIAL_MIN_INTERVAL = 1_000_000_000 / 1;

    /**
     * The y-coordinate of the highest ground blocks.
     */
    public static final int GROUND_LEVEL = 50;

    /**
     * The number of extra chunks in each direction from the player's chunk to keep loaded.
     */
    private static final int LOAD_DISTANCE = Renderer.RENDER_DISTANCE;

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
    protected long getInitialMinInterval() {
        return Generator.INITIAL_MIN_INTERVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initInThread() {
        return;
    }

    /**
     * If necessary, generate any unloaded chunks near the player and load them into the world.
     */
    @Override
    public void tick(final double elapsedTime) {
        final Vector playerChunkPos = Chunk.toChunkPos(this.world.getPlayer().getPosition());
        int numberDone = 0;
        for (final Vector chunkPos : Chunk.getChunkPosNearby(playerChunkPos, Generator.LOAD_DISTANCE)) {
            if (!world.chunkLoaded(chunkPos)) {
                world.loadChunk(chunkPos, this.generateChunk(chunkPos));
                numberDone++;
                if (numberDone == 3) {
                    return;
                }
            }
        }
    }

    private Chunk generateChunk(final Vector pos) {
        final Chunk chunk = new Chunk(pos);
        final double[] blockPos = new double[3];
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        for (blockPos[0] = pos.getX(); blockPos[0] < pos.getX() + Chunk.SIZE; blockPos[0]++) {
            for (blockPos[1] = pos.getY(); blockPos[1] < pos.getY() + Chunk.SIZE
                    && blockPos[1] < Generator.GROUND_LEVEL; blockPos[1]++) {
                for (blockPos[2] = pos.getZ(); blockPos[2] < pos.getZ() + Chunk.SIZE; blockPos[2]++) {
                    if (random.nextBoolean()) {
                        final Vector blockPosVec = new Vector(blockPos[0], blockPos[1], blockPos[2]);
                        final Block block = new DirtBlock(chunk, blockPosVec);
                        chunk.putBlock(blockPosVec, block);
                    }
                }
            }
        }
        return chunk;
    }
}
