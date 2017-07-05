package com.sheaconlon.realcraft.simulator;

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
     * Calculate the minimum translation vector of an object of interest with some other object.
     *
     * If the object of interest is penetrating the other object, then the minimum translation vector gives the
     * translation of minimal magnitude that can be applied to the object of interest to make it stop penetrating
     * the other object. If the object of interest is not penetrating the other object, then null is returned.
     * @param a The object of interest.
     * @param b The other object.
     * @return The minimum translation vector of {@code a} with {@code b}, or null if there is none.
     */
    public static double[] calcMTV(final WorldObject a, final WorldObject b) {
        // TODO
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
}
