package com.sheaconlon.realcraft.world;

import java.util.HashMap;
import java.util.Map;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.utilities.BlockPosition;
import com.sheaconlon.realcraft.utilities.ChunkPosition;

/**
 * The world.
 */
public class World {
    /**
     * The chunks of the world.
     *
     * If loaded, the chunk at block position (x, y, z) is the value of the entry with key
     * {@code new BlockPosition(x, y, z)}.
     */
    private final Map<ChunkPosition, Chunk> chunks;

    /**
     * The chunk generator for the world.
     */
    private final ChunkGenerator chunkGenerator;

    /**
     * Construct a world.
     */
    public World(final ChunkGenerator chunkGenerator) {
        this.chunks = new HashMap<>();
        this.chunkGenerator = chunkGenerator;
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block at {@code pos}.
     */
    Block getBlock(final BlockPosition pos) {
        this.ensureChunkLoaded(pos.toChunkPosition());
        return this.chunks.get(pos.toChunkPosition()).getBlock(pos);
    }

    /**
     * Ensure that a chunk at some position is loaded into {@link #chunks}.
     *
     * If the chunk is not already loaded, uses {@link #chunkGenerator} to generate it.
     * @param pos The position.
     */
    private void ensureChunkLoaded(final ChunkPosition pos) {
        if (!this.chunks.containsKey(pos)) {
            final Chunk chunk = this.chunkGenerator.getChunk(pos);
            this.chunks.put(pos, chunk);
        }
    }
}
