package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.world.Chunk;

/**
 * A renderer of a chunk.
 */
public class ChunkRenderer extends VBORenderer<Chunk> {
    /**
     * Create a renderer for some chunk.
     *
     * An OpenGL context must be current.
     * @param chunk The chunk.
     */
    public ChunkRenderer(final Chunk chunk) {
        super(chunk);
    }

    /**
     * Render the chunk.
     * @param chunk The chunk.
     */
    void render(final Chunk chunk) {
        this.render();
    }
}
