package com.sheaconlon.realcraft.world;

import java.util.HashMap;
import java.util.Map;
import com.sheaconlon.realcraft.blocks.Block;

/**
 * The world.
 */
public class World {
    /**
     * The chunks of the world.
     *
     * If loaded, the chunk at (x, y, z) is the value of the entry with key {@code new int[]{x, y, z}}.
     */
    private final Map<int[], Chunk> chunks;

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
     * Get the block at position (x, y, z).
     * @param x The x-coordinate of the desired block.
     * @param y The y-coordinate of the desired block.
     * @param z The z-coordinate of the desired block.
     * @return The desired block.
     */
    Block getBlock(final int x, final int y, final int z) {
        final int chunkX = Chunk.blockToChunkCoordinate(x);
        final int chunkY = Chunk.blockToChunkCoordinate(y);
        final int chunkZ = Chunk.blockToChunkCoordinate(z);
        this.ensureChunkLoaded(chunkX, chunkY, chunkZ);
        final int[] chunkCoordinates = new int[]{chunkX, chunkY, chunkZ};
        return this.chunks.get(chunkCoordinates).getBlock(x, y, z);
    }

    /**
     * Ensure that a desired chunk is loaded into {@link #chunks}.
     *
     * If not already loaded, uses {@link #chunkGenerator} to generate the desired chunk.
     * @param x The x-coordinate of the desired chunk. See {@link Chunk#x}.
     * @param y The y-coordinate of the desired chunk. See {@link Chunk#y}.
     * @param z The z-coordinate of the desired chunk. See {@link Chunk#z}.
     */
    private void ensureChunkLoaded(final int x, final int y, final int z) {
        final int[] chunkCoordinates = new int[]{y, y, z};
        if (!this.chunks.containsKey(chunkCoordinates)) {
            final Chunk chunk = this.chunkGenerator.getChunk(x, y, z);
            this.chunks.put(chunkCoordinates, chunk);
        }
    }
}
