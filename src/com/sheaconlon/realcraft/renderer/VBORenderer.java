package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.positioning.Position;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * A renderer of some {@link Iterable<Quad>} thing which uses VBOs.
 */
public abstract class VBORenderer<T extends Iterable<Quad>> extends Renderer<T> {
    /**
     * The number of floats used to specify a vertex's position.
     */
    private static final int FLOATS_PER_POSITION = 3;

    /**
     * The number of floats used to specify a quad's color.
     */
    private static final int FLOATS_PER_COLOR = 3;

    /**
     * The number of floats used to specify a vertex's normal.
     */
    private static final int FLOATS_PER_NORMAL = 3;

    /**
     * The number of bytes in a float.
     */
    private static final int BYTES_PER_FLOAT = Float.SIZE / Byte.SIZE;

    /**
     * The total number of floats used to specify a vertex.
     */
    private static final int FLOATS_PER_VERTEX = FLOATS_PER_POSITION + FLOATS_PER_COLOR + FLOATS_PER_NORMAL;

    /**
     * The total number of bytes used to specify a vertex.
     */
    private static final int BYTES_PER_VERTEX = FLOATS_PER_VERTEX * BYTES_PER_FLOAT;

    /**
     * Allocate a new VBO and return its OpenGL handle.
     *
     * An OpenGL context must be current.
     * @return The OpenGL handle of a new VBO.
     */
    private static int allocateVBO() {
        final IntBuffer vboHandleBuffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(vboHandleBuffer);
        return vboHandleBuffer.get();
    }

    /**
     * The OpenGL handle for this VBO renderer's VBO.
     */
    private final int vboHandle;

    /**
     * The number of vertices in this VBO renderer's VBO.
     */
    private int numVertices;

    /**
     * Create a VBO renderer for some thing.
     *
     * An OpenGL context must be current.
     * @param thing The thing.
     */
    protected VBORenderer(final T thing) {
        super(thing);
        this.vboHandle = VBORenderer.allocateVBO();
        final FloatBuffer dataBuffer = this.createBuffer(thing);
        this.writeData(dataBuffer);
    }

    /**
     * Create a buffer holding the vertex data of some thing and set this VBO renderer's {@link #numVertices}
     * appropriately.
     * @param thing The thing.
     * @return A buffer holding the vertex data of the thing.
     */
    private FloatBuffer createBuffer(final Iterable<Quad> thing) {
        final List<Float> data = new ArrayList<>();
        this.numVertices = 0;
        for (final Quad quad : thing) {
            final float[] color = quad.getColor();
            for (final Vertex vertex : quad.getVertices()) {
                this.numVertices++;
                final Position position = vertex.getPosition();
                data.add((float)position.getX());
                data.add((float)position.getY());
                data.add((float)position.getZ());
                data.add(color[0]);
                data.add(color[1]);
                data.add(color[2]);
                final float[] normal = vertex.getNormal();
                data.add(normal[0]);
                data.add(normal[1]);
                data.add(normal[2]);
            }
        }
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(data.size());
        for (final float entry : data) {
            buffer.put(entry);
        }
        buffer.flip();
        return buffer;
    }

    /**
     * Write a buffer to this VBO renderer's VBO.
     *
     * An OpenGL context must be current.
     * @param buffer The buffer.
     */
    private void writeData(final FloatBuffer buffer) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Render the thing.
     *
     * An OpenGL context must be current.
     */
    @Override
    void render() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
        GL11.glVertexPointer(VBORenderer.FLOATS_PER_POSITION, GL11.GL_FLOAT, VBORenderer.BYTES_PER_VERTEX, 0);
        GL11.glColorPointer(VBORenderer.FLOATS_PER_COLOR, GL11.GL_FLOAT, VBORenderer.BYTES_PER_VERTEX,
                VBORenderer.FLOATS_PER_POSITION * VBORenderer.BYTES_PER_FLOAT);
        GL11.glNormalPointer(GL11.GL_FLOAT, VBORenderer.BYTES_PER_VERTEX,
                (VBORenderer.FLOATS_PER_POSITION + VBORenderer.FLOATS_PER_COLOR) * VBORenderer.BYTES_PER_FLOAT);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, this.numVertices);
    }
}
