package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.Worker;
import com.sheaconlon.realcraft.ui.Window;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.utilities.PositionUtilities;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A pre-renderer, which prepares VBOs for the renderer.
 */
public class Prerenderer extends Worker {
    /**
     * The number of chunks in each direction from the player's chunk that pre-renderers should pre-render.
     */
    private static final int PRERENDER_DISTANCE = Renderer.RENDER_DISTANCE;

    /**
     * The world this pre-renderer should pre-render.
     */
    private final World world;

    /**
     * The renderer this pre-renderer should load with VBOs.
     */
    private final Renderer renderer;

    /**
     * Create a pre-renderer.
     * @param world See {@link #world}.
     */
    public Prerenderer(final World world, final Renderer renderer) {
        this.world = world;
        this.renderer = renderer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inThreadInitialize() {
        final boolean initSuccess = GLFW.glfwInit();
        final long windowHandle = GLFW.glfwCreateWindow(1, 1, "q", MemoryUtil.NULL, MemoryUtil.NULL);
        GLFW.glfwMakeContextCurrent(windowHandle);
        GL.createCapabilities();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick() {
        final double[] playerPos = this.world.getPlayer().getPosition();
        final int[] playerChunkPos = PositionUtilities.toChunkPosition(playerPos);
        for (final int[] renderChunkPos : PositionUtilities.getNearbyChunkPositions(playerChunkPos, Prerenderer.PRERENDER_DISTANCE)) {
            // TODO
        }
    }

    /**
     * Pre-render a chunk.
     * @param pos The position of the anchor point of the chunk.
     * @return A VBO for the chunk.
     */
    private VertexBufferObject prerenderChunk(final int[] pos) {
        final Chunk chunk = this.world.getChunk(pos);
        final List<float[][]> vertexDataList = new ArrayList<>();
        for (final Iterator<WorldObject> iterator = chunk.getContents(); iterator.hasNext(); ) {
            final WorldObject obj = iterator.next();
            for (final float[][] singleVertexData : obj.getVertexData()) {
                final float[][] singleVertexDataAbsolute = new float[][]{
                        ArrayUtilities.add(singleVertexData[0], obj.getPosition()),
                        singleVertexData[1],
                        singleVertexData[2]
                };
                vertexDataList.add(singleVertexDataAbsolute);
            }
        }
        final float[][][] vertexData = vertexDataList.toArray(new float[][][]{});
        final VertexBufferObject vbo = new VertexBufferObject();
        vbo.write(vertexData);
        return vbo;
    }
}
