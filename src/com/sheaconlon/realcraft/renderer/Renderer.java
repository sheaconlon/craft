package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.Worker;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.utilities.PositionUtilities;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A renderer.
 */
public class Renderer extends Worker {
    /**
     * The number of frames to wait between sends of VBOs.
     */
    private static final int SEND_INTERVAL = 60;

    /**
     * A renderer's return value for {@link #getInitialMinInterval()}.
     *
     * The value is 0 because a renderer synchronizes itself with screen refreshes and should not be artificially
     * slowed down.
     */
    private static final long INITIAL_MIN_INTERVAL = 0;

    /**
     * The number of VBOs that a renderer should stock its empty VBO list with on each refill.
     */
    private static final int TARGET_NUM_EMPTY_VBOS = 10;

    /**
     * The world.
     */
    private final World world;

    /**
     * The vertical field of view of player in radians.
     */
    private static final float VERTICAL_FIELD_OF_VIEW = (float)(Math.PI / 2);

    /**
     * The distance of the nearest visible objects.
     */
    private static final float NEAR_CUTOFF = 0.05f;

    /**
     * The distance of the furthest visible objects.
     */
    private static final float FAR_CUTOFF = 1000;

    /**
     * A vector giving the direction of the sun. The trailing zero indicates to OpenGL that the light is
     * directional, not positional.
     */
    private static final float[] SUN_DIRECTION = new float[]{-1, 3, 1, 0};

    /**
     * The color of sunlight, in RGBA format.
     */
    private static final float[] SUNLIGHT_COLOR = new float[]{1, 0.965f, 0.847f, 1};

    /**
     * The position of the player's eye, relative to the player's anchor point.
     */
    private static final double[] PLAYER_EYE_POSITION =
            new double[]{Player.HIT_BOX_DIMS[0] / 2, Player.HIT_BOX_DIMS[1] * 0.9, Player.HIT_BOX_DIMS[2] / 2};

    /**
     * The number of extra chunks to render in each direction from the player's chunk.
     */
    public static final int RENDER_DISTANCE = 2;

    /**
     * The color of the sky, in RGBA format.
     */
    private static final float[] SKY_COLOR = new float[]{0.435f, 0.675f, 0.969f};

    /**
     * Configure OpenGL hints and enable certain capabilities.
     */
    private static void configureOpenGL() {
        GLFW.glfwSwapInterval(1);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    /**
     * Set up a perspective projection.
     * @param windowDimensions The width and height of the window to render in.
     */
    private static void setProjection(final int[] windowDimensions) {
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        final float aspectRatio = (float)((double)windowDimensions[0] / (double)windowDimensions[1]);
        matrix.setPerspective(Renderer.VERTICAL_FIELD_OF_VIEW, aspectRatio, Renderer.NEAR_CUTOFF,
                Renderer.FAR_CUTOFF);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    /**
     * Set the lighting to match the state of the Sun.
     */
    private static void setLighting() {
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, Renderer.SUN_DIRECTION);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Renderer.SUNLIGHT_COLOR);
    }

    /**
     * Clear the screen with the sky color.
     */
    private static void clear() {
        GL11.glClearColor(Renderer.SKY_COLOR[0], Renderer.SKY_COLOR[1], Renderer.SKY_COLOR[2], 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * The OpenGL handle of the window to render in.
     */
    private final long windowHandle;

    /**
     * The dimensions, in pixels, of the window's rendering area.
     */
    private final int[] windowDimensions;

    /**
     * The empty VBOs that this renderer has.
     */
    private final Deque<VertexBufferObject> emptyVBOs;

    /**
     * The written VBOs that this renderer has.
     */
    private final Map<List<Integer>, VertexBufferObject> writtenVBOs;

    /**
     * The sent VBOs that this renderer has.
     */
    private final Map<List<Integer>, VertexBufferObject> sentVBOs;

    /**
     * The number of frames that have been shown since the last VBO was sent.
     */
    private int framesSinceVBOSend;

    /**
     * Create a renderer.
     * @param windowHandle The OpenGL handle of the window to render in.
     * @param windowDimensions The dimensions, in pixels, of the window's rendering area.
     */
    public Renderer(final World world, final long windowHandle, final int[] windowDimensions) {
        this.world = world;
        this.windowHandle = windowHandle;
        this.windowDimensions = windowDimensions;
        this.emptyVBOs = new ConcurrentLinkedDeque<>();
        this.writtenVBOs = new ConcurrentHashMap<>();
        this.sentVBOs = new ConcurrentHashMap<>();
        this.framesSinceVBOSend = Renderer.SEND_INTERVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getInitialMinInterval() {
        return Renderer.INITIAL_MIN_INTERVAL;
    }

    /**
     * Return an empty VBO created by this renderer, or null if there is none.
     * @return An empty VBO created by this renderer, or null if there is none.
     */
    public VertexBufferObject getEmptyVBO() {
        return this.emptyVBOs.pollFirst();
    }

    /**
     * Receive a written VBO for some chunk.
     * @param pos The position of the anchor point of the chunk.
     * @param vbo The VBO.
     */
    public void receiveWrittenVBO(final int[] pos, final VertexBufferObject vbo) {
        final List<Integer> posList = ArrayUtilities.toList(pos);
        this.writtenVBOs.put(posList, vbo);
    }

    /**
     * Return whether this renderer has a written VBO for some chunk.
     * @param pos The position of the anchor point of the chunk.
     * @return Whether this renderer has a written VBO for the chunk.
     */
    public boolean hasWrittenVBO(final int[] pos) {
        final List<Integer> posList = ArrayUtilities.toList(pos);
        return this.sentVBOs.containsKey(posList) || this.writtenVBOs.containsKey(posList);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initInThread() {
        GLFW.glfwMakeContextCurrent(this.windowHandle);
        Renderer.configureOpenGL();
        Renderer.setProjection(this.windowDimensions);
        this.refillEmptyVBOs();
    }

    /**
     * Render the world.
     */
    public void tick(final double elapsedTime) {
        this.refillEmptyVBOs();
        this.setPerspective();
        Renderer.setLighting();
        Renderer.clear();
        this.sendVBO();
        final double[] playerPos = this.world.getPlayer().getPosition();
        final int[] playerChunkPos = PositionUtilities.toChunkPosition(playerPos);
        for (final int[] renderChunkPos : PositionUtilities.getNearbyChunkPositions(playerChunkPos, Renderer.RENDER_DISTANCE)) {
            final List<Integer> renderChunkPosList = ArrayUtilities.toList(renderChunkPos);
            if (this.sentVBOs.containsKey(renderChunkPosList)) {
                final VertexBufferObject vbo = this.sentVBOs.get(renderChunkPosList);
                vbo.render();
            }
        }
        GLFW.glfwSwapBuffers(this.windowHandle);
        this.framesSinceVBOSend++;
    }

    /**
     * Set the camera to match the player's perspective.
     */
    private void setPerspective() {
        final Player player = this.world.getPlayer();
        final double[] position = player.getPosition();
        final double xzOrientation = player.getXzOrientation();
        final double xzCrossOrientation = player.getXzCrossOrientation();
        final double[] eyePosition = ArrayUtilities.add(position, Renderer.PLAYER_EYE_POSITION);
        final double[] lookDisplacement = PositionUtilities.rotatePosition(new double[]{1, 0, 0}, xzOrientation,
                xzCrossOrientation);
        final double[] lookPosition = ArrayUtilities.add(eyePosition, lookDisplacement);
        final double[] upDirection = PositionUtilities.rotatePosition(new double[]{0, 1, 0}, xzOrientation,
                xzCrossOrientation);
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        matrix.setLookAt((float)eyePosition[0], (float)eyePosition[1], (float)eyePosition[2],
                (float)lookPosition[0], (float)lookPosition[1], (float)lookPosition[2],
                (float)upDirection[0], (float)upDirection[1], (float)upDirection[2]);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    /**
     * Refill this renderer's empty VBO list.
     */
    private void refillEmptyVBOs() {
        while (this.emptyVBOs.size() < Renderer.TARGET_NUM_EMPTY_VBOS) {
            this.emptyVBOs.addLast(new VertexBufferObject());
        }
    }

    /**
     * Possibly send a VBO, depending on how many frames have passed since a VBO was last sent.
     */
    private void sendVBO() {
        if (this.framesSinceVBOSend < Renderer.SEND_INTERVAL && this.ticks >= Worker.BEGINNING_TICKS) {
            return;
        }
        final double[] playerPos = this.world.getPlayer().getPosition();
        final int[] playerChunkPos = PositionUtilities.toChunkPosition(playerPos);
        for (final int[] chunkPos : PositionUtilities.getNearbyChunkPositions(playerChunkPos, Renderer.RENDER_DISTANCE)) {
            final List<Integer> chunkPosList = ArrayUtilities.toList(chunkPos);
            if (this.writtenVBOs.containsKey(chunkPosList)) {
                final VertexBufferObject vbo = this.writtenVBOs.remove(chunkPosList);
                final boolean success = vbo.send();
                if (success) {
                    this.sentVBOs.put(chunkPosList, vbo);
                }
                this.framesSinceVBOSend = 0;
                return;
            }
        }
    }
}
