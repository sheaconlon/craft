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
 * A renderer, which renders the world for the launcher.
 */
public class Renderer {
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

    // TODO: Respond to window resizes.
    // TODO: Restrict window fatness to avoid cheating.
    /**
     * Construct a renderer.
     */
    public Renderer(final long windowHandle, final int[] windowDimensions) {
        this.windowHandle = windowHandle;
        this.chunkRenderers = new HashMap<>();
        GLFW.glfwMakeContextCurrent(windowHandle);
        GLFW.glfwSwapInterval(1);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        Renderer.setProjection(windowDimensions);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_LIGHT0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, Renderer.SUN_DIRECTION);
        GL11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, Renderer.SUNLIGHT_COLOR);
    }

    // TODO: Render chunks relative to player, not origin.
    public void render(final World world) {
        Renderer.clear();
        final Player player = world.getPlayer();
        Renderer.setModelView(player.getAnchor(), player.getEyePosition(), player.getOrientation());
        final ChunkPosition playerChunkPosition = player.getAnchor().toChunkPosition();
        for (long x = -Renderer.RENDER_DISTANCE; x <= Renderer.RENDER_DISTANCE; x++){
            for (long y = -Renderer.RENDER_DISTANCE; y <= Renderer.RENDER_DISTANCE; y++){
                for (long z = -Renderer.RENDER_DISTANCE; z <= Renderer.RENDER_DISTANCE; z++){
                    System.out.printf("rendering chunk at (%d, %d, %d)...\n", x, y, z);
                    final ChunkPosition renderChunkPosition = new ChunkPosition(playerChunkPosition.getX() + x,
                            playerChunkPosition.getY() + y, playerChunkPosition.getZ() + z);
                    final Chunk renderChunk = world.getChunk(renderChunkPosition);
                    final ChunkRenderer chunkRenderer = this.getChunkRenderer(renderChunk);
                    chunkRenderer.render(renderChunk);
                    System.out.printf("done with chunk at (%d, %d, %d)...\n", x, y, z);
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
     * Set the OpenGL projection to
     * TODO: Finish this.
     */
    private static void setProjection(final int[] windowDimensions) {
        final FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        final Matrix4f matrix = new Matrix4f();
        final float aspectRatio = (float)((double)windowDimensions[0] / (double)windowDimensions[1]);
        matrix.setPerspective(Renderer.VERTICAL_FIELD_OF_VIEW, aspectRatio, Renderer.NEAR_CUTOFF,
                2 * Renderer.RENDER_DISTANCE * Chunk.SIZE);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadMatrixf(matrix.get(buffer));
    }

    /**
     * Set the OpenGL model view matrix to
     * TODO: Finish this.
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
        GL11.glClearColor(Renderer.SKY_COLOR[0], Renderer.SKY_COLOR[1], Renderer.SKY_COLOR[2], 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }
}
