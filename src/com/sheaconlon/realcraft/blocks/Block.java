package com.sheaconlon.realcraft.blocks;

import com.sheaconlon.realcraft.renderer.Quad;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.simulator.Hitbox;
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
    /**
     * The normal vector for the front face of a block.
     */
    private static final float[] FRONT_NORMAL = new float[]{0, 0, 1};

    /**
     * The normal vector for the left face of a block.
     */
    private static final float[] LEFT_NORMAL = new float[]{-1, 0, 0};

    /**
     * The normal vector for the back face of a block.
     */
    private static final float[] BACK_NORMAL = new float[]{0, 0, -1};

    /**
     * The normal vector for the right face of a block.
     */
    private static final float[] RIGHT_NORMAL = new float[]{1, 0, 0};

    /**
     * The normal vector for the top face of a block.
     */
    private static final float[] TOP_NORMAL = new float[]{0, 1, 0};

    /**
     * The normal vector for the bottom face of a block.
     */
    private static final float[] BOTTOM_NORMAL = new float[]{0, -1, 0};

    private static final float[] BLACK = new float[]{0, 0, 0};
    private static final List<Quad> FACES = Collections.unmodifiableList(Stream.of(
            new Quad(Stream.of(
                    new Vertex(new float[]{0, 0, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{1, 0, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{1, 1, 0}, BLACK, FRONT_NORMAL),
                    new Vertex(new float[]{0, 1, 0}, BLACK, FRONT_NORMAL)
            ).collect(Collectors.toList())),
            new Quad(Stream.of(
                    new Vertex(new float[]{0, 0, -1}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{0, 0, 0}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{0, 1, 0}, BLACK, LEFT_NORMAL),
                    new Vertex(new float[]{0, 1, -1}, BLACK, LEFT_NORMAL)
            ).collect(Collectors.toList())),
            new Quad(Stream.of(
                    new Vertex(new float[]{1, 0, -1}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{0, 0, -1}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{0, 1, -1}, BLACK, BACK_NORMAL),
                    new Vertex(new float[]{1, 1, -1}, BLACK, BACK_NORMAL)
            ).collect(Collectors.toList())),
            new Quad(Stream.of(
                    new Vertex(new float[]{1, 0, 0}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{1, 0, -1}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{1, 1, -1}, BLACK, RIGHT_NORMAL),
                    new Vertex(new float[]{1, 1, 0}, BLACK, RIGHT_NORMAL)
            ).collect(Collectors.toList())),
            new Quad(Stream.of(
                    new Vertex(new float[]{0, 1, 0}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{1, 1, 0}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{1, 1, -1}, BLACK, TOP_NORMAL),
                    new Vertex(new float[]{0, 1, -1}, BLACK, TOP_NORMAL)
            ).collect(Collectors.toList())),
            new Quad(Stream.of(
                    new Vertex(new float[]{0, 0, -1}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{1, 0, -1}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{1, 0, 0}, BLACK, BOTTOM_NORMAL),
                    new Vertex(new float[]{0, 0, 0}, BLACK, BOTTOM_NORMAL)
            ).collect(Collectors.toList()))
    ).collect(Collectors.toList()));

    /**
     * The initial velocity of a block.
     */
    private static final Vector INIT_VELOCITY = new Vector(0, 0, 0);

    /**
     * The initial xz orientation of a block.
     */
    private static final double INIT_ORIENT = 0;

    private final List<Hitbox> hitboxes;

    /**
     * Create a block.
     * @param pos See {@link WorldObject#getPos()}.
     */
    public Block(final Vector pos) {
        super(pos, INIT_ORIENT, INIT_VELOCITY);
        this.hitboxes = new LinkedList<>();
        this.hitboxes.add(new Hitbox(this, new Vector(-0.5, -0.5, -0.5), new Vector(1, 1, 1)));
    }

    /**
     * {@inheritDoc}
     */
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
        return Collections.unmodifiableList(this.hitboxes);
    }
}
