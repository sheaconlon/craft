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

/**
 * A renderer.
 */
public class Renderer extends Worker {
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
    private static final double[] PLAYER_EYE_POSITION = new double[]{0.5, 1.2, 0.5};

    /**
     * The number of extra chunks to render in each direction from the player's chunk.
     */
    public static final int RENDER_DISTANCE = 1;

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
     * The vbos that this renderer has created.
     */
    private final Map<List<Integer>, VertexBufferObject> chunkVbos;

    /**
     * Create a renderer.
     * @param windowHandle The OpenGL handle of the window to render in.
     * @param windowDimensions The dimensions, in pixels, of the window's rendering area.
     */
    public Renderer(final World world, final long windowHandle, final int[] windowDimensions) {
        this.world = world;
        this.windowHandle = windowHandle;
        this.chunkVbos = new HashMap<>();
        this.windowDimensions = windowDimensions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inThreadInitialize() {
        GLFW.glfwMakeContextCurrent(this.windowHandle);
        Renderer.configureOpenGL();
        Renderer.setProjection(this.windowDimensions);
    }

    /**
     * Render the world.
     */
    public void tick() {
        this.setPerspective();
        Renderer.setLighting();
        Renderer.clear();
        final double[] playerPosition = this.world.getPlayer().getPosition();
        final int[] playerChunkPosition = PositionUtilities.toChunkPosition(playerPosition);
        final int[] deltaChunkPosition = new int[playerChunkPosition.length];
        for (deltaChunkPosition[0] = -Renderer.RENDER_DISTANCE; deltaChunkPosition[0] <= Renderer.RENDER_DISTANCE; deltaChunkPosition[0]++) {
            for (deltaChunkPosition[1] = -Renderer.RENDER_DISTANCE; deltaChunkPosition[1] <= Renderer.RENDER_DISTANCE; deltaChunkPosition[1]++) {
                for (deltaChunkPosition[2] = -Renderer.RENDER_DISTANCE; deltaChunkPosition[2] <= Renderer.RENDER_DISTANCE; deltaChunkPosition[2]++) {
                    final int[] renderChunkPosition = ArrayUtilities.add(playerChunkPosition, deltaChunkPosition);
                    final int[] renderPosition = ArrayUtilities.multiply(renderChunkPosition, Chunk.SIZE);
                    this.renderChunk(renderPosition);
                }
            }
        }
        GLFW.glfwSwapBuffers(this.windowHandle);
    }

    /**
     * Set the camera to match the player's perspective.
     */
    private void setPerspective() {
        final Player player = this.world.getPlayer();
        final double[] position = player.getPosition();
        final double orientation = player.getOrientation();
        final double lookDirection = player.getLookDirection();
        final double[] eyePosition = new double[]{
                position[0] + Renderer.PLAYER_EYE_POSITION[0],
                position[1] + Renderer.PLAYER_EYE_POSITION[1],
                position[2] + Renderer.PLAYER_EYE_POSITION[2]
        };
        final double[] gazePosition = new double[]{
                eyePosition[0] + Math.cos(orientation)*Math.cos(lookDirection),
                eyePosition[1] + Math.sin(lookDirection),
                eyePosition[2] + Math.sin(orientation)*Math.cos(lookDirection)
        };
        final double[] up = new double[]{
                Math.cos(orientation)*Math.cos(lookDirection + Math.PI / 2),
                Math.sin(lookDirection + Math.PI / 2),
                Math.sin(orientation)*Math.cos(lookDirection + Math.PI / 2)
        };
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        matrix.setLookAt((float)eyePosition[0], (float)eyePosition[1], (float)eyePosition[2], (float)gazePosition[0],
                (float)gazePosition[1], (float)gazePosition[2], (float)up[0], (float)up[1], (float)up[2]);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    /**
     * Render a chunk.
     * @param position The position of the anchor point of the chunk.
     */
    private void renderChunk(final int[] position) {
        final VertexBufferObject vbo = this.getVBO(position);
        vbo.render();
    }

    /**
     * Get the VBO for a chunk.
     *
     * Will return the already-existent VBO in {@link #chunkVbos} or create a new one if one does not exist.
     * @param position The position of the anchor point of the chunk.
     * @return A VBO for the chunk.
     */
    private VertexBufferObject getVBO(final int[] position) {
        final List<Integer> positionList = ArrayUtilities.toList(position);
        if (!this.chunkVbos.containsKey(positionList)) {
            final Chunk chunk = this.world.getChunk(position);
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
            this.chunkVbos.put(positionList, vbo);
        }
        return this.chunkVbos.get(positionList);
    }
}
