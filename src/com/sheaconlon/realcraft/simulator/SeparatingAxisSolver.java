package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.utilities.PositionUtilities;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * A separating axis solver, useful for collision detection and resolution.
 *
 * Implemented using the description at http://www.dyn4j.org/2010/01/sat/.
 */
public class SeparatingAxisSolver {
    /**
     * The vertex positions of the unit cube.
     */
    private static final double[][] UNIT_CUBE_VERTEX_POSITIONS = new double[][]{
            new double[]{0, 0, 0},
            new double[]{0, 0, 1},
            new double[]{0, 1, 0},
            new double[]{0, 1, 1},
            new double[]{1, 0, 0},
            new double[]{1, 0, 1},
            new double[]{1, 1, 0},
            new double[]{1, 1, 1}
    };

    /**
     * The positive-pointing edge normals of the unit cube.
     */
    private static final double[][] UNIT_CUBE_POSITIVE_EDGE_NORMALS = new double[][]{
            new double[]{1, 0, 0},
            new double[]{0, 1, 0},
            new double[]{0, 0, 1}
    };

    /**
     * Calculate the minimum translation vector for a pair of objects.
     *
     * If a pair of objects are colliding, then the minimum translation vector gives the amount and direction that the first
     * object must be moved so the pair of objects no longer collide.
     * @param first The first object.
     * @param other The other object.
     * @return The minimum translation vector of {@code first} with {@code other} if they are colliding, or null if they are
     * not colliding.
     */
    public static double[] calcMTV(final WorldObject first, final WorldObject other) {
        final double[][] hitBoxFirst = SeparatingAxisSolver.calcHitBox(first);
        final double[][] hitBoxOther = SeparatingAxisSolver.calcHitBox(other);
        final double[][] edgeNormalsFirst = SeparatingAxisSolver.calcEdgeNormals(first);
        final double[][] edgeNormalsOther = SeparatingAxisSolver.calcEdgeNormals(other);
        double[] mtvDirection = null;
        double mtvMagnitude = Double.POSITIVE_INFINITY;
        for (final double[] edgeNormal : ArrayUtilities.concat(edgeNormalsFirst, edgeNormalsOther)) {
            final double[] projectionFirst = SeparatingAxisSolver.calcProjection(hitBoxFirst, edgeNormal);
            final double[] projectionOther = SeparatingAxisSolver.calcProjection(hitBoxOther, edgeNormal);
            final double firstOtherOverlap = projectionFirst[1] - projectionOther[0];
            if (firstOtherOverlap > 0) {
                if (firstOtherOverlap < mtvMagnitude) {
                    mtvDirection = ArrayUtilities.multiply(edgeNormal, -1);
                    mtvMagnitude = firstOtherOverlap;
                }
                continue;
            }
            final double otherFirstOverlap = projectionOther[1] - projectionFirst[0];
            if (otherFirstOverlap > 0) {
                if (otherFirstOverlap < mtvMagnitude) {
                    mtvDirection = ArrayUtilities.copy(edgeNormal);
                    mtvMagnitude = otherFirstOverlap;
                }
                continue;
            }
            return null;
        }
        if (mtvDirection == null) {
            return null;
        }
        return ArrayUtilities.multiply(mtvDirection, Math.sqrt(mtvMagnitude));
    }

    /**
     * Calculate the vertex positions of the hit box of an object.
     * @param o The object.
     * @return The vertex positions of the hit box of the object. An array of vertex positions, where a
     * vertex position is an array of x-, y-, and z-coordinates.
     */
    private static double[][] calcHitBox(final WorldObject o) {
        return PositionUtilities.translatePositions(
                PositionUtilities.rotatePositions(
                    PositionUtilities.scalePositions(
                        SeparatingAxisSolver.UNIT_CUBE_VERTEX_POSITIONS,
                        o.getHitBoxDims()
                    ),
                    o.getXzOrientation(),
                    o.getXzCrossOrientation()
                ),
            o.getPosition()
        );
    }

    /**
     * Calculate the positive edge normals of the hit box of an object.
     * @param o The object.
     * @return The positive edge normals of the hit box of the object. An array of vectors, where a vector is an
     * array of x-, y-, and z-components.
     */
    private static double[][] calcEdgeNormals(final WorldObject o) {
        return PositionUtilities.rotatePositions(
                SeparatingAxisSolver.UNIT_CUBE_POSITIVE_EDGE_NORMALS,
                o.getXzOrientation(),
                o.getXzCrossOrientation()
        );
    }

    /**
     * Calculate the projection of some hit box onto some axis.
     * @param hitBox The hit box, represented as an array of vertex positions. Must have at least one vertex.
     * @param axis The axis, represented as a unit-length vector.
     * @return The projection of {@code hitBox} onto {@code axis}, as an array with the start and end positions
     * of the projection. The start and end positions are represented as distances along {@code axis} from the origin.
     */
    private static double[] calcProjection(final double[][] hitBox, final double[] axis) {
        final double[] hitBoxProjection = ArrayUtilities.multiply(hitBox[0], axis);
        for (int i = 1; i < hitBox.length; i++) {
            final double[] vertexProjection = ArrayUtilities.multiply(hitBox[i], axis);
            hitBoxProjection[0] = Math.min(hitBoxProjection[0], vertexProjection[0]);
            hitBoxProjection[1] = Math.max(hitBoxProjection[1], vertexProjection[1]);
        }
        return hitBoxProjection;
    }
}
