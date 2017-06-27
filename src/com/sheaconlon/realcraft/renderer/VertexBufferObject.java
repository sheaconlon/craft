package com.sheaconlon.realcraft.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * An OpenGL vertex buffer object.
 */
class VertexBufferObject {
    /**
     * The number of elements per vertex position in a VBO.
     */
    private static final int ELEMENTS_PER_POSITION = 3;

    /**
     * The number of elements per vertex color in a VBO.
     */
    private static final int ELEMENTS_PER_COLOR = 3;

    /**
     * The number of elements per vertex normal in a VBO.
     */
    private static final int ELEMENTS_PER_NORMAL = 3;

    /**
     * The number of elements per vertex in a VBO.
     */
    private static final int ELEMENTS_PER_VERTEX = VertexBufferObject.ELEMENTS_PER_POSITION
            + VertexBufferObject.ELEMENTS_PER_COLOR + VertexBufferObject.ELEMENTS_PER_NORMAL;

    /**
     * The number of bytes per element in a VBO.
     */
    private static final int BYTES_PER_ELEMENT = 4;

    /**
     * The number of bytes per vertex in a VBO.
     */
    private static final int BYTES_PER_VERTEX = VertexBufferObject.ELEMENTS_PER_VERTEX
            * VertexBufferObject.BYTES_PER_ELEMENT;

    /**
     * The OpenGL handle of this VBO.
     */
    private int handle;

    /**
     * The number of vertices in this VBO.
     */
    private int numVertices;

    /**
     * Create a vertex buffer object.
     *
     * An OpenGL context must be current.
     */
    VertexBufferObject() {
        final IntBuffer handleBuffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(handleBuffer);
        this.handle = handleBuffer.get();
        this.write(new float[][][]{});
    }

    /**
     * Write some vertex data to this VBO.
     *
     * An OpenGL context must be current.
     * @param vertexData The vertex data.
     */
    void write(final float[][][] vertexData) {
        this.numVertices = 0;
        final int elements = vertexData.length * VertexBufferObject.ELEMENTS_PER_VERTEX;
        final FloatBuffer vertexDataBuffer = BufferUtils.createFloatBuffer(elements);
        for (final float[][] vertex : vertexData) {
            this.numVertices++;
            for (final float[] piece : vertex) {
                vertexDataBuffer.put(piece);
            }
        }
        vertexDataBuffer.flip();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexDataBuffer, GL15.GL_STATIC_DRAW);
    }

    /**
     * Render this VBO.
     *
     * An OpenGL context must be current.
     */
    void render() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
        GL11.glVertexPointer(VertexBufferObject.ELEMENTS_PER_POSITION, GL11.GL_FLOAT,
                VertexBufferObject.BYTES_PER_VERTEX, 0);
        GL11.glColorPointer(VertexBufferObject.ELEMENTS_PER_COLOR, GL11.GL_FLOAT,
                VertexBufferObject.BYTES_PER_VERTEX,
                VertexBufferObject.ELEMENTS_PER_POSITION * VertexBufferObject.BYTES_PER_ELEMENT);
        GL11.glNormalPointer(GL11.GL_FLOAT, VertexBufferObject.BYTES_PER_VERTEX,
                (VertexBufferObject.ELEMENTS_PER_POSITION + VertexBufferObject.ELEMENTS_PER_COLOR)
                        * VertexBufferObject.BYTES_PER_ELEMENT);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, this.numVertices);
    }
}
