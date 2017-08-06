package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.blocks.DirtBlock;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;

import java.util.HashSet;
import java.util.Set;

public class Generator extends Worker {
    // ##### PRIVATE STATIC FINAL #####
    private static final PerlinNoiseGenerator TERRAIN_NOISE_GENERATOR =
            new PerlinNoiseGenerator(0.25, 5, x -> x);

    // ##### PRIVATE FINAL #####
    private final Set<Chunk> generated;

    // ##### CONSTRUCTORS #####
    public Generator() {
        this.generated = new HashSet<>();
    }

    // ##### PRIVATE STATIC FINAL #####
    private static final int RADIUS = 1;

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
        for (final Vector blockAnchor : chunk.blockAnchors()) {
            final double terrainNoise = TERRAIN_NOISE_GENERATOR.noise(blockAnchor);
            if (terrainNoise > 0.5) {
                chunk.putBlock(new DirtBlock(blockAnchor));
            }
        }
    }
}