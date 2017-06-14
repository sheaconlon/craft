package com.sheaconlon.realcraft.world;

import java.util.HashMap;
import java.util.Map;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entity.entities.Player;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.positioning.ChunkPosition;

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
     * The player of this world.
     */
    private final Player player;

    /**
     * Construct a world.
     */
    public World(final ChunkGenerator chunkGenerator) {
        this.chunks = new HashMap<>();
        this.chunkGenerator = chunkGenerator;
        this.player = this.chunkGenerator.getPlayer();
    }

    /**
     * Get the player of this world.
     * @return The player of this world.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block at {@code pos}.
     */
    Block getBlock(final BlockPosition pos) {
        return this.getChunk(pos.toChunkPosition()).getBlock(pos);
    }

    /**
     * Get the chunk at some position.
     * @param position The position.
     * @return The chunk.
     */
    public Chunk getChunk(final ChunkPosition position) {
        if (!this.chunks.containsKey(position)) {
            final Chunk chunk = this.chunkGenerator.getChunk(position);
            this.chunks.put(position, chunk);
        }
        return this.chunks.get(position);
    }
}
