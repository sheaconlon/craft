package com.sheaconlon.realcraft.renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A thing which knows how to render some thing using VBOs.
 */
public abstract class VBORenderer<T> extends Renderer2<T> {
    /**
     * The OpenGL handle for this VBO renderer's VBO.
     */
    private final int vboHandle;

    /**
     * The number of elements in this renderer's VBO.
     */
    private int vboLength;

    /**
     * Create a VBO renderer for some thing.
     *
     * An OpenGL context must be current.
     * @param thing The thing.
     */
    protected VBORenderer(final T thing) {
        super(thing);
        final IntBuffer vboHandleBuffer = BufferUtils.createIntBuffer(1);
        GL15.glGenBuffers(vboHandleBuffer);
        this.vboHandle = vboHandleBuffer.get();
    }

    /**
     * Render the previously sent VBO data.
     *
     * Must be called after {@link #sendVBOData(FloatBuffer)}. An OpenGL context must be current.
     */
    @Override
    void render() {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, (3 * 4) + (3 * 4) + (3 * 4), 0);
        GL11.glColorPointer(3, GL11.GL_FLOAT, (3 * 4) + (3 * 4) + (3 * 4), (3 * 4));
        GL11.glNormalPointer(GL11.GL_FLOAT, (3 * 4) + (3 * 4) + (3 * 4), (3 * 4) + (3 * 4));
        GL11.glDrawArrays(GL11.GL_QUADS, 0, this.vboLength);
    }

    /**
     * Send VBO data to the GPU.
     *
     * An OpenGL context must be current.
     * @param data The data.
     */
    protected void sendVBOData(final FloatBuffer data) {
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vboHandle);
        this.vboLength = data.remaining() / (3 + 3 + 3);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
    }
}
