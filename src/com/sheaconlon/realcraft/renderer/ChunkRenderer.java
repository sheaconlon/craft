package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.world.Chunk;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class ChunkRenderer extends VBORenderer<Chunk> {
    private final Chunk chunk;

    public ChunkRenderer(final Chunk chunk) {
        super(chunk);
        this.chunk = chunk;
        // TODO: Make ChunkRenderer render entities as well as blocks.
        // TODO: Make ChunkRenderer work for variable number of quads.
        final int quadCount = Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * 6;
        final int doublesPerQuad = 3 + 4 + 3;
        final DoubleBuffer vboData = BufferUtils.createDoubleBuffer(quadCount * doublesPerQuad);
        for (final Quad quad : chunk) {
            final double[] color = quad.getColor();
            for (final Vertex vertex : quad.getVertices()) {
                final Position position = vertex.getPosition();
                vboData.put(position.getX());
                vboData.put(position.getY());
                vboData.put(position.getZ());
                vboData.put(color);
                vboData.put(1);
                vboData.put(vertex.getNormal());
            }
        }
        vboData.limit(vboData.position());
        vboData.flip();
        this.sendVBOData(vboData);
    }

    void render(final Chunk chunk) {
        super.render();
    }
}
