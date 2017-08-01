package com.sheaconlon.realcraft.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A tester of vectors.
 */
public class VectorTester {
    private static final Vector POS_X = new Vector(1, 0, 0);
    private static final Vector POS_X_30_DEG = new Vector(Math.sqrt(3) / 2, 0, -1.0/2.0);
    private static final Vector NEG_Z = new Vector(0, 0, -1);
    private static final Vector NEG_Z_45_DEG = new Vector(-Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2);
    private static final Vector RANDOM = new Vector(43, 59, 30);
    private static final Vector RANDOM_7_RAD = new Vector(52.1, 59.0, -5.63); // Per http://www.wolframalpha.com/input/?i=rotate+(43,+59,+30)+by+7+radians+about+the+y-axis.

    @Test
    public void testRotateHorizontalPosX30Deg() {
        final Vector posX30 = Vector.rotateHorizontal(POS_X, Math.PI / 6);
        assertTrue(Vector.aboutEquals(POS_X_30_DEG, posX30, 0.001));
    }

    @Test
    public void testRotateHorizontalNegZ45Deg() {
        final Vector negZ45 = Vector.rotateHorizontal(NEG_Z, Math.PI / 4);
        assertTrue(Vector.aboutEquals(NEG_Z_45_DEG, negZ45, 0.001));
    }

    @Test
    public void testRotateHorizontalRandom7Rad() {
        final Vector random7rad = Vector.rotateHorizontal(RANDOM, 7);
        assertTrue(Vector.aboutEquals(RANDOM_7_RAD, random7rad, 0.1));
    }
}
