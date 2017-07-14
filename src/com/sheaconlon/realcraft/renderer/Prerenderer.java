package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A pre-renderer, which prepares VBOs for the renderer.
 */
public class Prerenderer extends Worker {
    /**
     * A pre-renderer's return value for {@link #getTargetFreq()}.
     */
    private static final long TARGET_FREQ = 1;

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

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL.MEDIUM;
    }

    @Override
    public String toString() {
        return "Prerenderer";
    }

    @Override
    public boolean needsMainThread() {
        return false;
    }

    @Override
    public boolean needsDedicatedThread() {
        return false;
    }

    @Override
    protected double getTargetFreq() {
        return Prerenderer.TARGET_FREQ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tick(final double elapsedTime) {
        final Vector playerPos = this.world.getPlayer().getPosition();
        final Vector playerChunkPos = Chunk.toChunkPos(playerPos);
        int numberDone = 0;
        for (final Vector renderChunkPos : Chunk.getChunkPosNearby(playerChunkPos, Prerenderer.PRERENDER_DISTANCE)) {
            if (world.chunkLoaded(renderChunkPos) && !renderer.hasWrittenVBO(renderChunkPos)) {
                final VertexBufferObject vbo = this.renderer.getEmptyVBO();
                if (vbo != null) {
                    this.prerenderChunk(renderChunkPos, vbo);
                    this.renderer.receiveWrittenVBO(renderChunkPos, vbo);
                }
                numberDone++;
                if (numberDone == 3) {
                    return;
                }
            }
        }
    }

    /**
     * Pre-render a chunk into a VBO.
     * @param chunkPos The position of the anchor point of the chunk.
     * @param vbo The VBO.
     */
    private void prerenderChunk(final Vector chunkPos, final VertexBufferObject vbo) {
        final Chunk chunk = this.world.getChunk(chunkPos);
        final List<float[][]> vertexDataList = new ArrayList<>();
        for (final Iterator<WorldObject> iterator = chunk.getContents(); iterator.hasNext(); ) {
            final WorldObject obj = iterator.next();
            for (final float[][] singleVertexData : obj.getVertexData()) {
                final Vector objPos = obj.getPosition();
                final float[][] singleVertexDataAbsolute = new float[][]{
                        new float[]{
                                (float)(singleVertexData[0][0] + objPos.getX()),
                                (float)(singleVertexData[0][1] + objPos.getY()),
                                (float)(singleVertexData[0][2] + objPos.getZ())
                        },
                        singleVertexData[1],
                        singleVertexData[2]
                };
                vertexDataList.add(singleVertexDataAbsolute);
            }
        }
        final float[][][] vertexData = vertexDataList.toArray(new float[][][]{});
        vbo.write(vertexData);
    }
}
