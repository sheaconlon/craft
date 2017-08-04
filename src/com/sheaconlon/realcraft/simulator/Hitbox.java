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

    /**
     * Get the minimum translation vector for this hitbox and some other hitbox it may be colliding with.
     *
     * The minimum translation vector is the smallest axis-aligned translation that could be applied to this hitbox to resolve
     * its collision with the other hitbox. If this hitbox is not colliding with the other hitbox, then it will be the zero
     * vector.
     * @param other The other hitbox.
     * @return The minimum translation vector for this hitbox and {@code other}.
     */
    public Vector minTranslation(final Hitbox other) {
        Vector minTrans = new Vector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        final boolean[] overlap = new boolean[]{false, false, false};
        final Vector[] thisBounds = this.getBounds();
        final double[] thisMins = thisBounds[0].toArray();
        final double[] thisMaxs = thisBounds[1].toArray();
        final Vector[] otherBounds = other.getBounds();
        final double[] otherMins = otherBounds[0].toArray();
        final double[] otherMaxs = otherBounds[1].toArray();
        for (int i = 0; i < 3; i++) {
            if (thisMaxs[i] > otherMins[i] && thisMaxs[i] <= otherMaxs[i]) {
                overlap[i] = true;
                final double[] trans = new double[3];
                trans[i] = -(thisMaxs[i] - otherMins[i]);
                final Vector transVec = new Vector(trans);
                if (transVec.sqMag() < minTrans.sqMag()) {
                    minTrans = transVec;
                }
            }
            if (thisMins[i] < otherMaxs[i] && thisMins[i] >= otherMins[i]) {
                overlap[i] = true;
                final double[] trans = new double[3];
                trans[i] = otherMaxs[i] - thisMins[i];
                final Vector transVec = new Vector(trans);
                if (transVec.sqMag() < minTrans.sqMag()) {
                    minTrans = transVec;
                }
            }
        }
        if (overlap[0] && overlap[1] && overlap[2]) {
            return minTrans;
        } else {
            return Vector.ZERO;
        }
    }
}
