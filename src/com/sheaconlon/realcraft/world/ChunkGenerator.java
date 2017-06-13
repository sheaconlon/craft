package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.positioning.ChunkPosition;

/**
 * A chunk generator. Creates the initial state of the world.
 */
public class ChunkGenerator {
    /**
     * The y-coordinate of the top of the ground.
     */
    private static final int GROUND_LEVEL = Chunk.SIZE / 2;

    /**
     * Generate the chunk at some position.
     *
     * @param chunkPos The position.
     * @return The chunk at {@code pos}.
     */
    Chunk getChunk(final ChunkPosition chunkPos) {
        final Chunk chunk = new Chunk(chunkPos);
        for (long bx = chunkPos.getX(); bx < chunkPos.getX() + Chunk.SIZE; bx++) {
            for (long bz = chunkPos.getZ(); bz < chunkPos.getZ() + Chunk.SIZE; bz++) {
                for (long by = chunkPos.getY(); by < chunkPos.getY() + Chunk.SIZE; by++) {
                    final BlockPosition blockPos = new BlockPosition(bx, by, bz);
                    Block block;
                    // Place dirt up to blockY = GROUND_LEVEL - 1 so that the highest dirt blocks extend to
                    // GROUND_LEVEL. For blockY <= GROUND_LEVEL - 1, place dirt blocks at every "even" position.
                    if (blockPos.getY() <= GROUND_LEVEL - 1 && blockPos.hashCode() % 2 == 0) {
                        block = new DirtBlock(blockPos);
                    } else {
                        block = new AirBlock(blockPos);
                    }
                    chunk.setBlock(blockPos, block);
                }
            }
        }
        return chunk;
    }
}
