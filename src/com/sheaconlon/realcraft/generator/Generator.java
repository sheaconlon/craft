package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.blocks.WoodBlock;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.LeafBlock;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;
import java.util.LinkedList;

/**
 * A generator. Responsible for procedurally generating chunks of the world and loading them into the world just in time
 * for the player to view them.
 */
public class Generator extends Worker {
    private static final double TREE_GENERATION_PROBABILITY = (double)4 / (double)(10 * 10);
    private static final int[] TREE_TRUNK_HEIGHTS = new int[]{4, 5, 5, 5, 6, 7};
    private static final double[] TREE_LEAF_RADII = new double[]{2.5, 3, 3, 3, 4, 5};

    /**
     * A generator's return value for {@link #getTargetFreq()}.
     */
    private static final double TARGET_FREQ = 4;

    /**
     * The y-coordinate of the highest ground blocks.
     */
    public static final int GROUND_LEVEL = 45;

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

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL.MEDIUM;
    }

    @Override
    public String toString() {
        return "Generator";
    }

    @Override
    public boolean needsMainThread() {
        return false;
    }

    @Override
    public boolean needsDedicatedThread() {
        return false;
    }

    @Override
    protected double getTargetFreq() {
        return Generator.TARGET_FREQ;
    }

    /**
     * If necessary, generate any unloaded chunks near the player and load them into the world.
     */
    @Override
    public void tick(final double elapsedTime) {
        final Vector playerChunkPos = Chunk.toChunkPos(this.world.getPlayer().getPos());
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
        this.generateGround(pos, chunk);
        this.generateTrees(pos, chunk);
        return chunk;
    }

    private void generateGround(final Vector pos, final Chunk chunk) {
        final double[] blockPos = new double[3];
        for (blockPos[0] = pos.getX(); blockPos[0] < pos.getX() + Chunk.SIZE; blockPos[0]++) {
            for (blockPos[1] = pos.getY(); blockPos[1] < pos.getY() + Chunk.SIZE
                    && blockPos[1] < Generator.GROUND_LEVEL; blockPos[1]++) {
                for (blockPos[2] = pos.getZ(); blockPos[2] < pos.getZ() + Chunk.SIZE; blockPos[2]++) {
                    final Vector blockPosVec = new Vector(blockPos[0], blockPos[1], blockPos[2]);
                    final Block block = new DirtBlock(blockPosVec);
                    chunk.putBlock(blockPosVec, block);
                }
            }
        }
    }

    private void generateTrees(final Vector pos, final Chunk chunk) {
        if (pos.getY() > GROUND_LEVEL || pos.getY() + Chunk.SIZE < GROUND_LEVEL) {
            return;
        }
        final Vector lo = Vector.setY(pos, GROUND_LEVEL);
        final Vector hi = Vector.add(Vector.setY(pos, GROUND_LEVEL), new Vector(Chunk.SIZE - 1, 0,Chunk.SIZE - 1));
        for (final Vector anchor : Vector.between(lo, hi)) {
            if (ThreadLocalRandom.current().nextDouble() < TREE_GENERATION_PROBABILITY) {
                this.generateTree(pos, anchor, chunk);
            }
        }
    }

    private void generateTree(final Vector pos, final Vector anchor, final Chunk chunk) {
        final int type = ThreadLocalRandom.current().nextInt(TREE_TRUNK_HEIGHTS.length);
        if (anchor.getX() < pos.getX() + TREE_LEAF_RADII[type]
                || anchor.getZ() < pos.getZ() + TREE_LEAF_RADII[type]
                || anchor.getX() > pos.getX() + Chunk.SIZE - 1 - TREE_LEAF_RADII[type]
                || anchor.getY() > pos.getY() + Chunk.SIZE - 1 - TREE_LEAF_RADII[type] - TREE_TRUNK_HEIGHTS[type]
                || anchor.getZ() > pos.getZ() + Chunk.SIZE - 1 - TREE_LEAF_RADII[type]) {
            return;
        }
        for (final Vector leafPos : Vector.around(Vector.changeY(anchor, TREE_TRUNK_HEIGHTS[type]), TREE_LEAF_RADII[type])) {
            if (chunk.getBlock(leafPos) instanceof AirBlock) {
                chunk.putBlock(leafPos, new LeafBlock(leafPos));
            }
        }
        for (final Vector trunkPos : Vector.between(anchor, Vector.changeY(anchor, TREE_TRUNK_HEIGHTS[type]))) {
            final Block old = chunk.getBlock(trunkPos);
            if (old instanceof LeafBlock || old instanceof AirBlock) {
                chunk.putBlock(trunkPos, new WoodBlock(trunkPos));
            }
        }
    }
}
