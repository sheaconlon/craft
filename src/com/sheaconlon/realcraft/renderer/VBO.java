package com.sheaconlon.realcraft.renderer;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * A wrapper for an OpenGL indexed vertex buffer object.
 */
public class VBO {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int BYTES_PER_INT = 4;

    private enum State {
        NOT_LINKED, LINKED_NOT_SENT, SENT
    }

    private int capacity;
    private State state;
    private ByteBuffer dataBuffer;
    private FloatBuffer dataBufferFloat;
    private ByteBuffer indexBuffer;
    private ShortBuffer indexBufferShort;
    private Thread glThread;
    private int vertexArrayHandle;
    private int dataBufferHandle;
    private int indexBufferHandle;
    private int numInstances;
    private int currIndex;
    private Map<Vertex, Integer> indices;

    /**
     * Create a new VBO.
     */
    public VBO(final int capacity) {
        this.capacity = capacity;
        this.state = State.NOT_LINKED;
        this.dataBuffer = BufferUtils.createByteBuffer(this.capacity * BYTES_PER_FLOAT
                * (Vertex.POSITION_SIZE + Vertex.COLOR_SIZE + Vertex.NORMAL_SIZE));
        this.dataBufferFloat = this.dataBuffer.asFloatBuffer();
        this.indexBuffer = BufferUtils.createByteBuffer(this.capacity * BYTES_PER_INT);
        this.indexBufferShort = this.indexBuffer.asShortBuffer();
        this.glThread = null;
        this.vertexArrayHandle = -1;
        this.dataBufferHandle = -1;
        this.numInstances = 0;
        this.currIndex = 0;
        this.indices = new HashMap<>();
    }

    /**
     * Link this VBO to an OpenGL VBO.
     *
     * This can be done only once. An OpenGL context must be current.
     */
    public void link() {
        if (!this.state.equals(State.NOT_LINKED)) {
            throw new RuntimeException("attempted to link a VBO twice");
        }
        this.state = State.LINKED_NOT_SENT;
        this.glThread = Thread.currentThread();

        // Get buffer handles.
        final IntBuffer bufferHandlesBuffer = BufferUtils.createIntBuffer(2);
        GL15.glGenBuffers(bufferHandlesBuffer);

        // Map data buffer.
        this.dataBufferHandle = bufferHandlesBuffer.get(0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.dataBufferHandle);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.dataBuffer, GL15.GL_STATIC_DRAW);
        this.dataBuffer = GL15.glMapBuffer(GL15.GL_ARRAY_BUFFER, GL15.GL_WRITE_ONLY);
        this.dataBufferFloat = this.dataBuffer.asFloatBuffer();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Map index buffer.
        this.indexBufferHandle = bufferHandlesBuffer.get(1);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBufferHandle);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer, GL15.GL_STATIC_DRAW);
        this.indexBuffer = GL15.glMapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, GL15.GL_WRITE_ONLY);
        this.indexBufferShort = this.indexBuffer.asShortBuffer();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Get vertex array handle.
        final IntBuffer vertexArrayHandleBuffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(vertexArrayHandleBuffer);
        this.vertexArrayHandle = vertexArrayHandleBuffer.get();
    }

    /**
     * Write a vertex to this VBO.
     *
     * Can be called only after {@link #link()} and before {@link #send()}.
     * @param vertex The vertex.
     */
    public void write(final Vertex vertex) {
        if (this.state.equals(State.NOT_LINKED)) {
            throw new RuntimeException("attempted to write to a VBO before linking it");
        }
        if (this.state.equals(State.SENT)) {
            throw new RuntimeException("attempted to write to a VBO after sending it");
        }
        if (this.isFull()) {
            throw new RuntimeException("attempted to write to a full VBO");
        }

        this.numInstances++;
        Integer index = this.indices.get(vertex);
        if (index == null) {
            index = this.currIndex;
            this.currIndex++;
            this.indices.put(vertex, index);
            this.dataBufferFloat.put(vertex.data());
        }
        this.indexBufferShort.put((short)(int)index);
    }

    /**
     * Return whether this VBO has reached its capacity.
     * @return Whether this VBO has reached its capacity.
     */
    public boolean isFull() {
        return this.numInstances >= this.capacity;
    }

    /**
     * Send this VBO's data to the GPU.
     *
     * Can be called only by the thread which called {@link #link()}. Can be called only after {@link #link()}. This can be
     * done only once. An OpenGL context must be current.
     * @return Whether this VBO was sent successfully.
     */
    public boolean send() {
        if (this.state.equals(State.NOT_LINKED)) {
            throw new RuntimeException("attempted to send a VBO before linking it");
        }
        if (this.state.equals(State.SENT)) {
            throw new RuntimeException("attempted to send a VBO twice");
        }
        if (!Thread.currentThread().equals(this.glThread)) {
            throw new RuntimeException("attempted to send a VBO from a different thread than the one that linked the VBO");
        }
        this.state = State.SENT;

        // Bind the vertex array.
        GL30.glBindVertexArray(this.vertexArrayHandle);

        // Unmap the data buffer.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.dataBufferHandle);
        boolean success = GL15.glUnmapBuffer(GL15.GL_ARRAY_BUFFER);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        this.dataBuffer = null;
        this.dataBufferFloat = null;

        // Unmap the index buffer.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBufferHandle);
        success &= GL15.glUnmapBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        this.indexBuffer = null;
        this.indexBufferShort = null;

        // Unbind the vertex array.
        GL30.glBindVertexArray(0);

        return success;
    }

    /**
     * Render this VBO.
     *
     * Can be called only by the thread which called {@link #link()}. Can be called only after {@link #send()}. An OpenGL
     * context must be current.
     */
    public void render() {
        if (!this.state.equals(State.SENT)) {
            throw new RuntimeException("attempted to render a VBO that was not sent");
        }
        if (!Thread.currentThread().equals(this.glThread)) {
            throw new RuntimeException("attempted to render a VBO from a different thread than the one that linked the VBO");
        }

        // Bind the vertex array.
        GL30.glBindVertexArray(this.vertexArrayHandle);

        // Bind the data buffer.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.dataBufferHandle);

        // Set up pointers.
        final int bytesPerPosition = Vertex.POSITION_SIZE * BYTES_PER_FLOAT;
        final int bytesPerColor = Vertex.COLOR_SIZE * BYTES_PER_FLOAT;
        final int bytesPerNormal = Vertex.NORMAL_SIZE * BYTES_PER_FLOAT;
        final int bytesPerVertex = bytesPerPosition + bytesPerColor + bytesPerNormal;
        GL11.glVertexPointer(Vertex.POSITION_SIZE, GL11.GL_FLOAT, bytesPerVertex, 0);
        GL11.glColorPointer(Vertex.COLOR_SIZE, GL11.GL_FLOAT, bytesPerVertex, bytesPerPosition);
        GL11.glNormalPointer(GL11.GL_FLOAT, bytesPerVertex, (bytesPerPosition + bytesPerColor));

        // Bind the index buffer.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBufferHandle);

        // Draw.
        GL11.glDrawElements(GL11.GL_QUADS, this.numInstances, GL11.GL_UNSIGNED_SHORT, 0);

        //Unbind the index buffer.
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // Unbind the data buffer.
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        // Unbind the vertex array.
        GL30.glBindVertexArray(0);
    }
}
