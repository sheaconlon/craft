package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.entity.entities.Player;
import com.sheaconlon.realcraft.positioning.BlockPosition;
import com.sheaconlon.realcraft.positioning.ChunkPosition;
import com.sheaconlon.realcraft.positioning.Position;

/**
 * A chunk generator, which generates the initial state of chunks on demand.
 */
public class ChunkGenerator {
    /**
     * The initial position of the player.
     */
    public static final Position PLAYER_INITIAL_POSITION = new Position(0, 0, 0);

    /**
     * The y-coordinate of the top of the ground.
     */
    private static final int GROUND_LEVEL = Chunk.SIZE / 2;

    /**
     * The player of the world being generated.
     */
    private final Player player;

    /**
     * Construct a chunk generator.
     */
    public ChunkGenerator() {
        this.player = new Player(ChunkGenerator.PLAYER_INITIAL_POSITION);
    }

    /**
     * Get the player of the world being generated.
     * @return The player of the world being generated.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Generate the chunk at some position.
     *
     * The world will consist of many layers of dirt blocks at even hash code block positions, then infinitely many
     * air blocks.
     * @param position The position.
     * @return The chunk at {@code position}.
     */
    Chunk getChunk(final ChunkPosition position) {
        System.out.printf("generating chunk at (%d, %d, %d)...\n", position.getX(), position.getY(), position.getZ());
        final Chunk chunk = new Chunk(position);
        for (long x = 0; x < Chunk.SIZE; x++) {
            for (long z = 0; z < Chunk.SIZE; z++) {
                for (long y = 0; y < Chunk.SIZE; y++) {
                    final BlockPosition blockPosition = new BlockPosition(x + position.getX(),
                            y + position.getY(), z + position.getZ());
                    Block block;
                    // Place dirt at every even hash code block position. The dirt blocks should be placed at
                    // positions up to GROUND_LEVEL - 1 so that the highest dirt blocks' upper surfaces are at
                    // GROUND_LEVEL.
                    if (blockPosition.getY() <= GROUND_LEVEL - 1 && blockPosition.hashCode() % 2 == 0) {
                        block = new DirtBlock(blockPosition);
                    } else {
                        block = new AirBlock(blockPosition);
                    }
                    chunk.setBlock(blockPosition, block);
                }
            }
        }
        if (position.equals(ChunkGenerator.PLAYER_INITIAL_POSITION.toChunkPosition())) {
            this.placePlayer(chunk);
        }
        System.out.printf("done with chunk at (%d, %d, %d)...\n", position.getX(), position.getY(), position.getZ());
        return chunk;
    }

    /**
     * Place the player at {@link ChunkGenerator#PLAYER_INITIAL_POSITION} in a chunk.
     * @param chunk The chunk.
     */
    private void placePlayer(final Chunk chunk) {
        // TODO: Choose a randomized, safe place instead of a preset, possibly dangerous place.
        chunk.addEntity(this.player);
    }
}
