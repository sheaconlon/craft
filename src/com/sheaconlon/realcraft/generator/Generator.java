package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Generator extends Worker {
    // ##### PRIVATE STATIC FINAL #####
    private static final PerlinNoiseGenerator HEIGHT_MAP_GENERATOR =
            new PerlinNoiseGenerator(0.005, 3, x -> (x + 1) / 2);
    private static final double HEIGHT_MAP_MINIMUM = 0;
    private static final double HEIGHT_MAP_MAXIMUM = 100;
    private static final double HEIGHT_MAP_RANGE = HEIGHT_MAP_MAXIMUM - HEIGHT_MAP_MINIMUM;

    // ##### PRIVATE FINAL #####
    private final Set<Chunk> generated;

    // ##### CONSTRUCTORS #####
    public Generator() {
        this.generated = new HashSet<>();
    }

    // ##### PRIVATE STATIC FINAL #####
    private static final int RADIUS = Renderer.RENDER_DISTANCE;

    // ##### WORKER OVERRIDES #####
    private static final PRIORITY_LEVEL PRIORITY_LEVEL = Worker.PRIORITY_LEVEL.MEDIUM;

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL;
    }

    private static final String NAME = "Generator";

    @Override
    public String toString() {
        return NAME;
    }

    private static final boolean NEEDS_MAIN_THREAD = false;

    @Override
    public boolean needsMainThread() {
        return NEEDS_MAIN_THREAD;
    }

    private static final boolean NEEDS_DEDICATED_THREAD = false;

    @Override
    public boolean needsDedicatedThread() {
        return NEEDS_DEDICATED_THREAD;
    }

    private static final int CHUNKS_PER_TICK = 3;

    @Override
    protected void tick(double interval) {
        final Chunk playerChunk = Chunk.containingChunk(Player.PLAYER.getPos());
        for (final Chunk chunkNearPlayer : playerChunk.chunksNearby(RADIUS)) {
            if (!this.generated.contains(chunkNearPlayer)) {
                this.generate(chunkNearPlayer);
                this.generated.add(chunkNearPlayer);
            }
        }
    }

    private static final double TARGET_FREQ = 4;

    @Override
    protected double getTargetFreq() {
        return TARGET_FREQ;
    }

    // ##### GENERATION #####
    private void generate(final Chunk chunk) {
        final Map<Vector, Integer> heightCache = new HashMap<Vector, Integer>();
        for (final Vector blockAnchor : chunk.blockAnchors()) {
            final Vector horizontalBlockAnchor = Vector.setY(blockAnchor, 0);
            Integer height = heightCache.get(horizontalBlockAnchor);
            if (height == null) {
                final double noise = HEIGHT_MAP_GENERATOR.noise(horizontalBlockAnchor);
                height = (int)(noise * HEIGHT_MAP_RANGE + HEIGHT_MAP_MINIMUM);
                heightCache.put(horizontalBlockAnchor, height);
            }
            if (blockAnchor.getY() <= height) {
                chunk.putBlock(new DirtBlock(blockAnchor));
            }
        }
    }
}