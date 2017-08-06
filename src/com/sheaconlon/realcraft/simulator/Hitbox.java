package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.utilities.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * An axis-aligned rectangular prism.
 *
 * Defines the boundaries of an object for the purpose of physical simulation.
 */
public class Hitbox {
    private final WorldObject object;
    private final Vector position;
    private final Vector dims;

    /**
     * Create a hitbox.
     * @param object The object that the hitbox is for. The position and orientation of the hitbox will track
     *            {@code object}'s position and orientation.
     * @param position The position of the small x, small y, small z corner of this hitbox, relative to the anchor
     *            point of {@code object}.
     * @param dims The dimensions of the hitbox. If {@code object} is at position {@code (x, y, z)}, then one corner
     *             of the hitbox will be at (x, y, z) and one corner of the hitbox will be at
     *             {@code (x + dims.getX(), y + dims.getY(), z + dims.getZ())}.
     */
    public Hitbox(final WorldObject object, final Vector position, final Vector dims) {
        this.object = object;
        this.position = position;
        this.dims = dims;
    }

    /**
     * Get the bounds of this hitbox.
     * @return The minimal coordinate values over the corners of this hitbox and the maximal coordinate values
     *         over the corners of this hitbox.
     */
    Vector[] getBounds() {
        // Lacking rotation and translation. Relative to anchor. Does not account for height.
        final Vector[] bottomFaceCorners = new Vector[]{
                new Vector(0, 0, 0),
                new Vector(this.dims.getX(), 0, 0),
                new Vector(this.dims.getX(), 0, this.dims.getZ()),
                new Vector(0, 0, this.dims.getZ())
        };
        // Lacking rotation. Relative to anchor. Does not account for height.
        for (int i = 0; i < bottomFaceCorners.length; i++) {
            bottomFaceCorners[i] = Vector.add(bottomFaceCorners[i], this.position);
        }
        // Relative to anchor. Does not account for height.
        for (int i = 0; i < bottomFaceCorners.length; i++) {
            bottomFaceCorners[i] =
                    Vector.rotateHorizontal(bottomFaceCorners[i], this.object.getOrient());
        }
        // Does not account for height.
        final Vector[] bounds = Vector.bounds(bottomFaceCorners);
        bounds[0] = Vector.add(bounds[0], this.object.getPos());
        bounds[1] = Vector.add(bounds[1], this.object.getPos());
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
     Vector minTranslation(final Hitbox other) {
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

    /**
     * Get the anchor points of the blocks which intersect this hitbox.
     * @return The anchor points of the blocks which intersect this hitbox.
     */
    public Vector[] intersectingBlockAnchors() {
         final List<Vector> intersectingBlockAnchors = new ArrayList<>();
         final Vector[] bounds = this.getBounds();
         bounds[0] = Vector.round(bounds[0]);
         for (final Vector intersectingBlockAnchor : Vector.between(bounds[0], bounds[1])) {
             intersectingBlockAnchors.add(intersectingBlockAnchor);
         }
         return intersectingBlockAnchors.toArray(new Vector[0]);
    }

    /**
     * Make a hitbox for some object.
     * @param hitbox The prototype hitbox.
     * @param object The object.
     * @return A new hitbox for {@code object} with {@code hitbox}'s position and dimensions.
     */
    public static Hitbox forObject(final Hitbox hitbox, final WorldObject object) {
        return new Hitbox(object, hitbox.position, hitbox.dims);
    }
}
