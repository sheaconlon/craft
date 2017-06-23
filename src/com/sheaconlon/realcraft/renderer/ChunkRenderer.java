package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.world.Chunk;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public class ChunkRenderer extends VBORenderer<Chunk> {
    private final Chunk chunk;

    public ChunkRenderer(final Chunk chunk) {
        super(chunk);
        this.chunk = chunk;
        // TODO: Make ChunkRenderer render entities as well as blocks.
        // TODO: Make ChunkRenderer work for variable number of quads.
        final int quadCount = Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * 6;
        final int doublesPerQuad = (3 + 3 + 3) * 4;
        final FloatBuffer vboData = BufferUtils.createFloatBuffer(quadCount * doublesPerQuad);
        for (final Quad quad : chunk) {
            final float[] color = quad.getColor();
            for (final Vertex vertex : quad.getVertices()) {
                final Position position = vertex.getPosition();
                vboData.put((float)position.getX());
                vboData.put((float)position.getY());
                vboData.put((float)position.getZ());
                vboData.put(color[0]);
                vboData.put(color[1]);
                vboData.put(color[2]);
                vboData.put(vertex.getNormal()[0]);
                vboData.put(vertex.getNormal()[1]);
                vboData.put(vertex.getNormal()[2]);
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
