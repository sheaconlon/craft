package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.world.Chunk;
import org.lwjgl.opengl.GL11;

public class ChunkRenderer {
    public ChunkRenderer(final Chunk chunk) {

    }

    void render(final Chunk chunk) {
        GL11.glBegin(GL11.GL_QUADS);
        for (final Quad quad : chunk) {
            final double[] color = quad.getColor();
            GL11.glColor3d(color[0], color[1], color[2]);
            for (final Vertex vertex : quad.getVertices()) {
                final double[] normal = vertex.getNormal();
                GL11.glNormal3d(normal[0], normal[1], normal[2]);
                GL11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, new float[]{(float)color[0], (float)color[1],
                        (float)color[2], 1});
                final Position pos = vertex.getPosition();
                GL11.glVertex3d(pos.getX(), pos.getY(), pos.getZ());
            }
        }
        GL11.glEnd();
    }
}
