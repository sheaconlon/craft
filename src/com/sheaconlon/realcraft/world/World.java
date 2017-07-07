package com.sheaconlon.realcraft.world;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.generator.Generator;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.utilities.PositionUtilities;

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
    private static final double PLAYER_SPAWN_XZ_ORIENTATION = 0;

    /**
     * The initial look direction of the player.
     */
    private static final double PLAYER_SPAWN_XZ_CROSS_ORIENTATION = 0;

    /**
     * The number of nanoseconds in a second.
     */
    private static final int NANOSECONDS_PER_SECOND = 1_000_000_000;

    /**
     * The number of seconds in a day.
     */
    public static final int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * The number of in-game seconds that pass per wall clock second.
     */
    private static final int TIME_RATIO = 60;

    /**
     * The y-coordinate of the highest blocks of the ground in a world.
     */
    private static final int GROUND_Y_MAX = 100;

    /**
     * The chunks of the world.
     */
    private final Map<List<Integer>, Chunk> chunks;

    /**
     * The wall time at which the world was created, in nanoseconds since some arbitrary fixed point.
     */
    private long originTime;

    /**
     * The player in this world.
     */
    private Player player;

    /**
     * Create a world.
     */
    public World() {
        this.chunks = new ConcurrentHashMap<>();
        this.originTime = System.nanoTime();
        this.player = this.generatePlayer();
    }

    /**
     * Get the chunk at some position.
     * @param position The position.
     * @return The chunk at the position.
     */
    public Chunk getChunk(final int[] position) {
        final List<Integer> positionList = ArrayUtilities.toList(position);
        return this.chunks.get(positionList);
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
        final double[] playerPosition = new double[]{
                distance * Math.cos(direction),
                Generator.GROUND_LEVEL + 1,
                distance * Math.sin(direction)
        };
        final Player player = new Player(playerPosition, World.PLAYER_SPAWN_XZ_ORIENTATION,
                World.PLAYER_SPAWN_XZ_CROSS_ORIENTATION);
        return player;
    }

    /**
     * Get the number of in-game seconds that have passed since the start of the current in-game day.
     */
    public int getTime() {
        final double elapsedWallNanoseconds = System.nanoTime() - this.originTime;
        final double elapsedWallSeconds = elapsedWallNanoseconds / World.NANOSECONDS_PER_SECOND;
        final long elapsedInGameSeconds = (long)(elapsedWallSeconds * World.TIME_RATIO);
        final int time = (int)(elapsedInGameSeconds % World.SECONDS_PER_DAY);
        return time;
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
    public boolean chunkLoaded(final int[] pos) {
        final List<Integer> posList = ArrayUtilities.toList(pos);
        return this.chunks.containsKey(posList);
    }

    /**
     * Load a chunk.
     * @param pos The position of the anchor point of the chunk.
     * @param chunk The chunk.
     */
    public void loadChunk(final int[] pos, final Chunk chunk) {
        final List<Integer> posList = ArrayUtilities.toList(pos);
        if (Arrays.equals(pos, PositionUtilities.toChunkPosition(this.player.getPosition()))) {
            chunk.addEntity(this.player);
        }
        this.chunks.put(posList, chunk);
    }

    /**
     * Get the block at some position.
     * @param pos The position.
     * @return The block at {@code pos}, or null if the chunk containing {@code pos} is not loaded.
     */
    public Block getBlock(final int[] pos) {
        final int[] chunkPos = PositionUtilities.toChunkPosition(pos);
        final Chunk chunk = this.getChunk(chunkPos);
        if (chunk == null) {
            return null;
        }
        return chunk.getBlock(pos);
    }
}
