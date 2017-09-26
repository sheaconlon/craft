package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.ListUtilities;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.world.Chunk;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A block, a cubical, grid-aligned object in the world.
 */
public abstract class Block extends WorldObject {
    // ##### PUBLIC STATIC FINAL #####
    public static final int SIZE = 1;

    // ##### PRIVATE STATIC FINAL #####
    private static final float[] FRONT_NORMAL = new float[]{0, 0, 1};
    private static final float[] LEFT_NORMAL = new float[]{-1, 0, 0};
    private static final float[] BACK_NORMAL = new float[]{0, 0, -1};
    private static final float[] RIGHT_NORMAL = new float[]{1, 0, 0};
    private static final float[] TOP_NORMAL = new float[]{0, 1, 0};
    private static final float[] BOTTOM_NORMAL = new float[]{0, -1, 0};
    private static final float[] BLACK = new float[]{0, 0, 0};
    private static final List<Quad> FACES = ListUtilities.unmodifiableList(
            new Quad(
                    new Vertex(new float[]{ 0,      0, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{SIZE,    0, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{SIZE, SIZE, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{ 0,   SIZE, 0}, BLACK, FRONT_NORMAL)
            ),
            new Quad(
                    new Vertex(new float[]{ 0,    0, -SIZE}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{ 0,    0,     0}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{ 0, SIZE,     0}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{ 0, SIZE, -SIZE}, BLACK, LEFT_NORMAL)
            ),
            new Quad(
                    new Vertex(new float[]{SIZE,    0, -SIZE}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{   0,    0, -SIZE}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{   0, SIZE, -SIZE}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{SIZE, SIZE, -SIZE}, BLACK, BACK_NORMAL)
            ),
            new Quad(
                    new Vertex(new float[]{ SIZE,    0,     0}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{ SIZE,    0, -SIZE}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{ SIZE, SIZE, -SIZE}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{ SIZE, SIZE,     0}, BLACK, RIGHT_NORMAL)
            ),
            new Quad(
                    new Vertex(new float[]{   0, SIZE,     0}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{SIZE, SIZE,     0}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{SIZE, SIZE, -SIZE}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{   0, SIZE, -SIZE}, BLACK, TOP_NORMAL)
            ),
            new Quad(
                    new Vertex(new float[]{   0, 0, -SIZE}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{SIZE, 0, -SIZE}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{SIZE, 0,     0}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{   0, 0,     0}, BLACK, BOTTOM_NORMAL)
            )
    );
    private static final Vector INITIAL_VELOCITY = new Vector(0, 0, 0);
    private static final double INITIAL_ORIENTATION = 0;
    private static final Vector HITBOX_POSITION = new Vector(-0.5, -0.5, -0.5);
    private static final Vector HITBOX_DIMENSIONS = new Vector(1, 1, 1);

    // ##### PRIVATE FINAL #####
    private final List<Hitbox> hitboxes;

    /**
     * Create a block.
     * @param anchor See {@link WorldObject#getPos()}.
     */
    public Block(final Vector anchor) {
        super(anchor, INITIAL_ORIENTATION, INITIAL_VELOCITY);
        this.hitboxes = ListUtilities.unmodifiableList(
                new Hitbox(this, HITBOX_POSITION, HITBOX_DIMENSIONS)
        );
    }

    @Override
    public List<Vertex> getVertices() {
        final List<Vertex> vertices = new LinkedList<>();
        final List<float[]> faceColors = this.getFaceColors();
        for (int face = 0; face < FACES.size(); face++) {
            vertices.addAll(FACES.get(face).withColor(faceColors.get(face)).vertices());
        }
        return vertices;
    }

    /**
     * Get the colors of the faces of this block.
     *
     * The color of a face is a float array consisting of its red, green, and blue components. The face colors
     * must appear in the order front, left, back, right, top, then bottom.
     * @return The colors of the faces of this block.
     */
    protected abstract List<float[]> getFaceColors();

    @Override
    public List<Hitbox> getHitboxes() {
        return this.hitboxes;
    }
}
