package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A pre-renderer, which prepares VBOs for the renderer.
 */
public class Prerenderer extends Worker {
    /**
     * A pre-renderer's return value for {@link #getTargetFreq()}.
     */
    private static final long TARGET_FREQ = 4;

    /**
     * The number of chunks in each direction from the player's chunk that pre-renderers should pre-render.
     */
    private static final int PRERENDER_DISTANCE = Renderer.RENDER_DISTANCE;

    /**
     * The renderer this pre-renderer should load with VBOs.
     */
    private final Renderer renderer;

    /**
     * Create a pre-renderer.
     */
    public Prerenderer(final Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL.MEDIUM;
    }

    @Override
    public String toString() {
        return "Prerenderer";
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
        return Prerenderer.TARGET_FREQ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick(final double elapsedTime) {
        final Vector playerPos = Player.PLAYER.getPos();
        final Chunk playerChunk = Chunk.containingChunk(playerPos);
        int numberDone = 0;
        for (final Chunk chunk : playerChunk.chunksNearby(Prerenderer.PRERENDER_DISTANCE)) {
            if (!renderer.hasWrittenVBO(chunk)) {
                final VBO vbo = this.renderer.getEmptyVBO();
                if (vbo != null) {
                    this.prerenderChunk(chunk, vbo);
                    this.renderer.receiveWrittenVBO(chunk, vbo);
                }
                numberDone++;
                if (numberDone == 3) {
                    return;
                }
            }
        }
    }

    /**
     * Pre-render a chunk into a VBO.
     * @param chunk The chunk.
     * @param vbo The VBO.
     */
    private void prerenderChunk(final Chunk chunk, final VBO vbo) {
        for (final Vector blockAnchor : chunk.blockAnchors()) {
            final Block block = chunk.getBlock(blockAnchor);
            for (Vertex vertex : block.getVertices()) {
                vertex = vertex.translate(block.getPos());
                vbo.write(vertex);
            }
        }
    }
}
