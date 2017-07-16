package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.utilities.Vector;

/**
 * An axis-aligned rectangular prism. It defines the boundaries of an object for the purpose of physical
 * simulation.
 */
public class Hitbox {
    private final WorldObject obj;
    private final Vector pos;
    private final Vector dims;

    /**
     * Create a hitbox.
     * @param obj The object that the hitbox is for. The position and orientation of the hitbox will track
     *            {@code obj}'s position and orientation.
     * @param pos The position of the small x, small y, small z corner of this hitbox, relative to the anchor
     *            point of {@code obj}.
     * @param dims The dimensions of the hitbox. If {@code obj} is at position {@code (x, y, z)}, then one corner
     *             of the hitbox will be at (x, y, z) and one corner of the hitbox will be at
     *             {@code (x + dims.getX(), y + dims.getY(), z + dims.getZ())}.
     */
    public Hitbox(final WorldObject obj, final Vector pos, final Vector dims) {
        this.obj = obj;
        this.pos = pos;
        this.dims = dims;
    }

    /**
     * @return The minimal coordinate values over the corners of this hitbox and the maximal coordinate values
     *         over the corners of this hitbox.
     */
    public Vector[] getBounds() {
        // Lacking rotation and translation. Relative to anchor. Does not account for height.
        final Vector[] bottomFaceCorners = new Vector[]{
                new Vector(0, 0, 0),
                new Vector(this.dims.getX(), 0, 0),
                new Vector(this.dims.getX(), 0, this.dims.getZ()),
                new Vector(0, 0, this.dims.getZ())
        };
        // Lacking rotation. Relative to anchor. Does not account for height.
        for (int i = 0; i < bottomFaceCorners.length; i++) {
            bottomFaceCorners[i] = Vector.add(bottomFaceCorners[i], this.pos);
        }
        // Relative to anchor. Does not account for height.
        for (int i = 0; i < bottomFaceCorners.length; i++) {
            bottomFaceCorners[i] =
                    Vector.rotateHorizontal(bottomFaceCorners[i], this.obj.getOrient());
        }
        // Does not account for height.
        final Vector[] bounds = Vector.bounds(bottomFaceCorners);
        bounds[0] = Vector.add(bounds[0], this.obj.getPos());
        bounds[1] = Vector.add(bounds[1], this.obj.getPos());
        // Correct.
        final Vector verticalDisp = new Vector(0, this.dims.getY(), 0);
        bounds[1] = Vector.add(bounds[1], verticalDisp);
        return bounds;
    }
}
