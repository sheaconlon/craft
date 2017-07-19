package com.sheaconlon.realcraft.world;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.UnloadedBlock;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.generator.Generator;
import com.sheaconlon.realcraft.utilities.Vector;

/**
 * The world.
 */
public class World {
    /**
     * The standard deviation of the distance of the player's spawn point from the origin.
     */
    private static final double PLAYER_SPAWN_DISTANCE_STDEV = 50;

    /**
     * The initial orientation of the player.
     */
    private static final double PLAYER_SPAWN_HORIZONTAL_ORIENTATION = 0;

    /**
     * The initial look direction of the player.
     */
    private static final double PLAYER_SPAWN_VERTICAL_ORIENTATION = 0;

    /**
     * The chunks of the world.
     */
    private final Map<Vector, Chunk> chunks;

    /**
     * The player in this world.
     */
    private Player player;

    /**
     * Create a world.
     */
    public World() {
        this.chunks = new ConcurrentHashMap<>();
        this.player = this.generatePlayer();
    }

    /**
     * Get the chunk at some position.
     * @param position The position.
     * @return The chunk at the position.
     */
    public Chunk getChunk(final Vector position) {
        return this.chunks.get(position);
    }

    /**
     * Generate the player.
     *
     * The player's position in the x-z-plane will be chosen randomly. The y-coordinate of the player's position
     * will be chosen to place the player on top of the ground.
     * @return The player.
     */
    private Player generatePlayer() {
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        final double distance = random.nextGaussian() * World.PLAYER_SPAWN_DISTANCE_STDEV;
        final double direction = random.nextDouble(Math.PI * 2);
        Vector playerPosition = new Vector(1, 0, 0);
        playerPosition = Vector.scale(playerPosition, distance);
        playerPosition = Vector.rotateHorizontal(playerPosition, direction);
        playerPosition = Vector.add(playerPosition, new Vector(0, Generator.GROUND_LEVEL + 1, 0));
        final Player player = new Player(playerPosition, World.PLAYER_SPAWN_HORIZONTAL_ORIENTATION,
                World.PLAYER_SPAWN_VERTICAL_ORIENTATION);
        return player;
    }

    /**
     * Getter for {@link #player}.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Return whether a chunk is loaded.
     * @param pos The position of the anchor point of the chunk.
     * @return Whether the chunk is loaded.
     */
    public boolean chunkLoaded(final Vector pos) {
        return this.chunks.containsKey(pos);
    }

    /**
     * Load a chunk.
     * @param pos The position of the anchor point of the chunk.
     * @param chunk The chunk.
     */
    public void loadChunk(final Vector pos, final Chunk chunk) {
        if (pos.equals(Chunk.toChunkPos(this.player.getPos()))) {
            chunk.addEntity(this.player);
        }
        this.chunks.put(pos, chunk);
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block at {@code pos}, or null if the chunk containing {@code pos} is not loaded.
     */
    public Block getBlock(final Vector pos) {
        final Chunk chunk = this.getChunk(Chunk.toChunkPos(pos));
        if (chunk == null) {
            return new UnloadedBlock(pos);
        }
        return chunk.getBlock(pos);
    }
}
