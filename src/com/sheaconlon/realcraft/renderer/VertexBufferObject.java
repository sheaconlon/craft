package com.sheaconlon.realcraft.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Represents an OpenGL vertex buffer object.
 */
class VertexBufferObject {
    /**
     * The number of floats per vertex position in a VBO.
     */
    private static final int FLOATS_PER_POSITION = 3;

    /**
     * The number of floats per vertex color in a VBO.
     */
    private static final int FLOATS_PER_COLOR = 3;

    /**
     * The number of floats per vertex normal in a VBO.
     */
    private static final int FLOATS_PER_NORMAL = 3;

    /**
     * The total number of floats per vertex in a VBO.
     */
    private static final int FLOATS_PER_VERTEX = VertexBufferObject.FLOATS_PER_POSITION
            + VertexBufferObject.FLOATS_PER_COLOR + VertexBufferObject.FLOATS_PER_NORMAL;

    /**
     * The number of bytes per float in a VBO.
     */
    private static final int BYTES_PER_FLOAT = 4;

    /**
     * The total number of bytes per vertex in a VBO.
     */
    private static final int BYTES_PER_VERTEX = VertexBufferObject.FLOATS_PER_VERTEX
            * VertexBufferObject.BYTES_PER_FLOAT;

    /**
     * The handle of the OpenGL VBO underlying this VBO.
     */
    private int handle;

    /**
     * The number of vertices in this VBO.
     */
    private int numVertices;

    /**
     * The thread which is the owner of OpenGL VBO underlying this VBO.
     */
    private final Thread owner;

    /**
     * A mapped buffer for the OpenGL VBO underlying this VBO.
     */
    private final FloatBuffer mappedBuffer;

    /**
     * Whether this VBO has been finalized and sent to the GPU.
     */
    private boolean sent;

    /**
     * Create a vertex buffer object.
     *
     * An OpenGL context must be current. The underlying OpenGL VBO will be owned by the thread which calls this.
     */
    VertexBufferObject() {
        final IntBuffer handleBuffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(handleBuffer);
        this.handle = handleBuffer.get();
        this.numVertices = 0;
        this.owner = Thread.currentThread();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
        this.mappedBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_STATIC_DRAW).asFloatBuffer();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this.sent = false;
    }

    /**
     * Write some vertex data to this VBO.
     *
     * An OpenGL context must be current. Can be called only before sending this VBO.
     * @param vertexData The vertex data.
     */
    void write(final float[][][] vertexData) {
        if (this.sent) {
            throw new RuntimeException("Attempted to call VertexBufferObject#write() after calling #send().");
        }
        for (final float[][] vertex : vertexData) {
            this.numVertices++;
            for (final float[] piece : vertex) {
                this.mappedBuffer.put(piece);
            }
        }
    }

    /**
     * Write one vertex's data to this VBO.
     *
     * An OpenGL context must be current. Can be called only before sending this VBO.
     * @param vertexData The vertex's data.
     */
    void write(final float[][] vertexData) {
        if (this.sent) {
            throw new RuntimeException("Attempted to call VertexBufferObject#write() after calling #send().");
        }
        this.numVertices++;
        for (final float[] piece : vertexData) {
            this.mappedBuffer.put(piece);
        }
    }

    /**
     * Finalize this VBO and send it to the GPU.
     *
     * An OpenGL context must be current. This method may be called only by the thread that owns the OpenGL VBO
     * underlying this VBO.
     * @return Whether this VBO was successfully finalized and sent to the GPU.
     */
    boolean send() {
        this.protect();
        if (this.sent) {
            return true;
        }
        this.sent = true;
        return GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
    }

    /**
     * Render this VBO.
     *
     * An OpenGL context must be current. This method may be called only by the thread that owns the OpenGL VBO
     * underlying this VBO. Can be called only after sending this VBO.
     */
    void render() {
        if (!this.sent) {
            throw new RuntimeException("Attempted to call VertexBufferObject#render() before calling #send().");
        }
        this.protect();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.handle);
        GL11.glVertexPointer(VertexBufferObject.FLOATS_PER_POSITION, GL11.GL_FLOAT,
                VertexBufferObject.BYTES_PER_VERTEX, 0);
        GL11.glColorPointer(VertexBufferObject.FLOATS_PER_COLOR, GL11.GL_FLOAT,
                VertexBufferObject.BYTES_PER_VERTEX,
                VertexBufferObject.FLOATS_PER_POSITION * VertexBufferObject.BYTES_PER_FLOAT);
        GL11.glNormalPointer(GL11.GL_FLOAT, VertexBufferObject.BYTES_PER_VERTEX,
                (VertexBufferObject.FLOATS_PER_POSITION + VertexBufferObject.FLOATS_PER_COLOR)
                        * VertexBufferObject.BYTES_PER_FLOAT);
        GL11.glDrawArrays(GL11.GL_QUADS, 0, this.numVertices);
    }

    /**
     * Protect this VBO from invalid access by throwing an error if the calling thread is not its owner.
     */
    private void protect() {
        if (!Thread.currentThread().equals(this.owner)) {
            throw new RuntimeException("Attempted to call VertexBufferObject#send() from a non-owner thread.");
        }
    }
}
