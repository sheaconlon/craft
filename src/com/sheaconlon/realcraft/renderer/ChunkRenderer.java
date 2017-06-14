package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.world.Chunk;
import org.lwjgl.opengl.GL11;

public class ChunkRenderer {
    void render(final Chunk chunk) {
        GL11.glBegin(GL11.GL_QUADS);
        for (final Quad quad : chunk.getQuads()) {
            final double[] color = quad.getColor();
            GL11.glColor3d(color[0], color[1], color[2]);
            for (final Vertex vertex : quad.getVertices()) {
                final Position pos = vertex.getPosition();
                GL11.glVertex3d(pos.getX(), pos.getY(), pos.getZ());
            }
        }
        GL11.glEnd();
    }
}
