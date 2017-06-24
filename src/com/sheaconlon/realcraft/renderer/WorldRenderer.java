package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.entity.entities.Player;
import com.sheaconlon.realcraft.positioning.ChunkPosition;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.World;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * A renderer for a world.
 */
public class WorldRenderer extends Renderer<World> {
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
     * The color of the sky, in RGBA format.
     */
    private static final float[] SKY_COLOR = new float[]{0.435f, 0.675f, 0.969f};

    /**
     * The size of the vertical field of view of the player, in radians.
     */
    private static final float VERTICAL_FIELD_OF_VIEW = (float)Math.toRadians(45);

    /**
     * The minimum distance at which an object will be rendered.
     */
    private static final float NEAR_CUTOFF = 0.3f;

    /**
     * The "radius" of the cubical subset of the world which should be rendered, in chunks.
     */
    private static final long RENDER_DISTANCE = 1;

    /**
     * The chunk renderers that this renderer has made.
     */
    private final Map<Chunk, ChunkRenderer> chunkRenderers;

    /**
     * The handle of the window to render in.
     */
    private final long windowHandle;

    /**
     * The world.
     */
    private final World world;

    // TODO: Respond to window resizes.
    // TODO: Restrict window fatness to avoid cheating.
    /**
     * Construct a world renderer.
     */
    public WorldRenderer(final long windowHandle, final int[] windowDimensions, final World world) {
        super(world);
        this.windowHandle = windowHandle;
        this.chunkRenderers = new HashMap<>();
        this.world = world;
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        WorldRenderer.setProjection(windowDimensions);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, WorldRenderer.SUN_DIRECTION);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, WorldRenderer.SUNLIGHT_COLOR);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
    }

    public void render() {
        WorldRenderer.clear();
        final Player player = this.world.getPlayer();
        WorldRenderer.setModelView(player.getAnchor(), player.getEyePosition(), player.getOrientation());
        final ChunkPosition playerChunkPosition = player.getAnchor().toChunkPosition();
        for (long x = -WorldRenderer.RENDER_DISTANCE; x <= WorldRenderer.RENDER_DISTANCE; x++){
            for (long y = -WorldRenderer.RENDER_DISTANCE; y <= WorldRenderer.RENDER_DISTANCE; y++){
                for (long z = -WorldRenderer.RENDER_DISTANCE; z <= WorldRenderer.RENDER_DISTANCE; z++){
                    final ChunkPosition renderChunkPosition = new ChunkPosition(playerChunkPosition.getX() + x,
                            playerChunkPosition.getY() + y, playerChunkPosition.getZ() + z);
                    final Chunk renderChunk = this.world.getChunk(renderChunkPosition);
                    final ChunkRenderer chunkRenderer = this.getChunkRenderer(renderChunk);
                    chunkRenderer.render(renderChunk);
                }
            }
        }
        GLFW.glfwSwapBuffers(this.windowHandle);
    }

    private ChunkRenderer getChunkRenderer(final Chunk chunk) {
        if (!this.chunkRenderers.containsKey(chunk)) {
            final ChunkRenderer chunkRenderer = new ChunkRenderer(chunk);
            this.chunkRenderers.put(chunk, chunkRenderer);
        }
        return this.chunkRenderers.get(chunk);
    }

    /**
     * Set up a perspective projection.
     */
    private static void setProjection(final int[] windowDimensions) {
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        final float aspectRatio = (float)((double)windowDimensions[0] / (double)windowDimensions[1]);
        matrix.setPerspective(WorldRenderer.VERTICAL_FIELD_OF_VIEW, aspectRatio, WorldRenderer.NEAR_CUTOFF,
                2 * WorldRenderer.RENDER_DISTANCE * Chunk.SIZE);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    /**
     * Set up a model view matrix that matches the player's current state.
     */
    private static void setModelView(final Position playerPosition, final Position playerEyePosition,
                                     final double playerOrientation) {
        final double x = playerEyePosition.getXAbsolute(playerPosition);
        final double y = playerEyePosition.getYAbsolute(playerPosition);
        final double z = playerEyePosition.getZAbsolute(playerPosition);
        final float centerX = (float)(x + Math.cos(playerOrientation));
        final float centerY = (float)y;
        final float centerZ = (float)(z + Math.sin(playerOrientation));
        final float upX = 0;
        final float upY = 1;
        final float upZ = 0;
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        matrix.setLookAt((float)x, (float)y, (float)z, centerX, centerY, centerZ, upX, upY, upZ);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    private static void clear() {
        GL11.glClearColor(WorldRenderer.SKY_COLOR[0], WorldRenderer.SKY_COLOR[1], WorldRenderer.SKY_COLOR[2], 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
