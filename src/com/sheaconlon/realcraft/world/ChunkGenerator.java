package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;

/**
 * A chunk generator. Creates the initial state of the world.
 */
public class ChunkGenerator {
    /**
     * The y-coordinate of the top of the ground.
     */
    private static final int GROUND_LEVEL = Chunk.SIZE / 2;

    /**
     * Generate a chunk.
     *
     * @param x The x-coordinate of the desired chunk. See {@link Chunk#x}.
     * @param y The y-coordinate of the desired chunk. See {@link Chunk#y}.
     * @param z The z-coordinate of the desired chunk. See {@link Chunk#z}.
     * @return The desired chunk.
     */
    Chunk getChunk(final int x, final int y, final int z) {
        final Chunk chunk = new Chunk(x, y, z);
        for (int blockX = x; blockX < x + Chunk.SIZE; blockX++) {
            for (int blockZ = z; blockZ < z + Chunk.SIZE; blockZ++) {
                for (int blockY = y; blockY < y + Chunk.SIZE; blockY++) {
                    Block block;
                    // Place dirt up to blockY = GROUND_LEVEL - 1 so that the highest dirt blocks extend to
                    // GROUND_LEVEL. For blockY <= GROUND_LEVEL - 1, place dirt blocks at every "even" position.
                    if (blockY <= GROUND_LEVEL - 1 && (blockX + blockY + blockZ) % 2 == 0) {
                        block = new DirtBlock(blockX, blockY, blockZ);
                    } else {
                        block = new AirBlock(blockX, blockY, blockZ);
                    }
                    chunk.setBlock(blockX, blockY, blockZ, block);
                }
            }
        }
        return chunk;
    }
}
