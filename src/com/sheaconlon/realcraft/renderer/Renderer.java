package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.entities.Player;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.utilities.Vector;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A renderer.
 */
public class Renderer extends Worker {
    /**
     * The number of frames to wait between sends of VBOs.
     */
    private static final int SEND_INTERVAL = 3;

    /**
     * A renderer's return value for {@link #getTargetFreq()}.
     *
     * The value is 0 because a renderer synchronizes itself with screen refreshes and should not be artificially
     * slowed down.
     */
    private static final double TARGET_FREQ = Double.POSITIVE_INFINITY;

    /**
     * The number of VBOs that a renderer should stock its empty VBO list with on each refill.
     */
    private static final int TARGET_NUM_EMPTY_VBOS = 100;

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
    private static final Vector PLAYER_EYE_POSITION = new Vector(0, 1.5, 0);

    /**
     * The number of extra chunks to render in each direction from the player's chunk.
     */
    public static final int RENDER_DISTANCE = 3;

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
    private final Deque<VBO> emptyVBOs;

    /**
     * The written VBOs that this renderer has.
     */
    private final Map<Vector, VBO> writtenVBOs;

    /**
     * The sent VBOs that this renderer has.
     */
    private final Map<Vector, VBO> sentVBOs;

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

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL.HIGH;
    }

    @Override
    public String toString() {
        return "Renderer";
    }

    @Override
    public boolean needsMainThread() {
        return false;
    }

    @Override
    public boolean needsDedicatedThread() {
        return true;
    }

    @Override
    protected double getTargetFreq() {
        return Renderer.TARGET_FREQ;
    }

    /**
     * Return an empty VBO created by this renderer, or null if there is none.
     * @return An empty VBO created by this renderer, or null if there is none.
     */
    public VBO getEmptyVBO() {
        return this.emptyVBOs.pollFirst();
    }

    /**
     * Receive a written VBO for some chunk.
     * @param pos The position of the anchor point of the chunk.
     * @param vbo The VBO.
     */
    public void receiveWrittenVBO(final Vector pos, final VBO vbo) {
        this.writtenVBOs.put(pos, vbo);
    }

    /**
     * Return whether this renderer has a written VBO for some chunk.
     * @param pos The position of the anchor point of the chunk.
     * @return Whether this renderer has a written VBO for the chunk.
     */
    public boolean hasWrittenVBO(final Vector pos) {
        return this.sentVBOs.containsKey(pos) || this.writtenVBOs.containsKey(pos);
    }

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
        final Vector playerPos = this.world.getPlayer().getPos();
        final Vector playerChunkPos = Chunk.toChunkPos(playerPos);
        for (final Vector renderChunkPos : Chunk.getChunkPosNearby(playerChunkPos, Renderer.RENDER_DISTANCE)) {
            if (this.sentVBOs.containsKey(renderChunkPos)) {
                final VBO vbo = this.sentVBOs.get(renderChunkPos);
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
        final Vector position = player.getPos();
        final double xzCrossOrientation = player.getVertOrient();
        final Vector eyePosition = Vector.add(position, Renderer.PLAYER_EYE_POSITION);
        final Vector lookDisplacement =
                Vector.rotateVertical(
                    Vector.rotateHorizontal(
                        new Vector(1, 0, 0),
                        player.getOrient()
                    ),
                    player.getVertOrient()
                );
        final Vector lookPosition = Vector.add(eyePosition, lookDisplacement);
        final Vector upDirection =
                Vector.rotateVertical(
                        Vector.rotateHorizontal(
                                new Vector(0, 1, 0),
                                player.getOrient()
                        ),
                        player.getVertOrient()
                );
        final DoubleBuffer buffer = BufferUtils.createDoubleBuffer(16);
        final Matrix4d matrix = new Matrix4d();
        matrix.setLookAt(eyePosition.getX(), eyePosition.getY(), eyePosition.getZ(),
                lookPosition.getX(), lookPosition.getY(), lookPosition.getZ(),
                upDirection.getX(), upDirection.getY(), upDirection.getZ());
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrixd(matrix.get(buffer));
    }

    /**
     * Refill this renderer's empty VBO list.
     */
    private void refillEmptyVBOs() {
        while (this.emptyVBOs.size() < Renderer.TARGET_NUM_EMPTY_VBOS) {
            // TODO: Possibly different capacity + splitting over multiple VBOs
            final VBO vbo = new VBO(Chunk.SIZE * Chunk.SIZE * Chunk.SIZE * 6 * 4);
            vbo.link();
            this.emptyVBOs.addLast(vbo);
        }
    }

    /**
     * Possibly send a VBO, depending on how many frames have passed since a VBO was last sent.
     */
    private void sendVBO() {
        if (this.framesSinceVBOSend < Renderer.SEND_INTERVAL) {
            return;
        }
        final Vector playerPos = this.world.getPlayer().getPos();
        final Vector playerChunkPos = Chunk.toChunkPos(playerPos);
        for (final Vector chunkPos : Chunk.getChunkPosNearby(playerChunkPos, Renderer.RENDER_DISTANCE)) {
            if (this.writtenVBOs.containsKey(chunkPos)) {
                final VBO vbo = this.writtenVBOs.remove(chunkPos);
                final boolean success = vbo.send();
                if (success) {
                    this.sentVBOs.put(chunkPos, vbo);
                }
                this.framesSinceVBOSend = 0;
                return;
            }
        }
    }
}
