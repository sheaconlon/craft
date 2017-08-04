package com.sheaconlon.realcraft.utilities;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A tester of vectors.
 */
public class VectorTester {
    private static final Vector POS_X = new Vector(1, 0, 0);
    private static final Vector POS_X_30_DEG = new Vector(Math.sqrt(3) / 2, 0, -1.0/2.0);

    @Test
    public void testRotateHorizontalPosX30Deg() {
        final Vector posX30 = Vector.rotateHorizontal(POS_X, Math.PI / 6);
        assertTrue(Vector.aboutEquals(POS_X_30_DEG, posX30, 0.001));
    }

    private static final Vector NEG_Z = new Vector(0, 0, -1);
    private static final Vector NEG_Z_45_DEG = new Vector(-Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2);

    @Test
    public void testRotateHorizontalNegZ45Deg() {
        final Vector negZ45 = Vector.rotateHorizontal(NEG_Z, Math.PI / 4);
        assertTrue(Vector.aboutEquals(NEG_Z_45_DEG, negZ45, 0.001));
    }

    private static final Vector RANDOM = new Vector(43, 59, 30);
    private static final Vector RANDOM_7_RAD_HORIZ = new Vector(52.1, 59.0, -5.63); // Per http://www.wolframalpha.com/input/?i=rotate+(43,+59,+30)+by+7+radians+about+the+y-axis.

    @Test
    public void testRotateHorizontalRandom7Rad() {
        final Vector random7rad = Vector.rotateHorizontal(RANDOM, 7);
        assertTrue(Vector.aboutEquals(RANDOM_7_RAD_HORIZ, random7rad, 0.1));
    }

    private static final Vector RANDOM_7_RAD_VERT = new Vector(-6.34, 72.7, 30.0); // Per http://www.wolframalpha.com/input/?i=rotate+(43,+59,+30)+by+7+radians+about+the+z-axis.

    @Test
    public void testRotateVerticalRandom7Rad() {
        final Vector random7rad = Vector.rotateVertical(RANDOM, 7);
        assertTrue(Vector.aboutEquals(RANDOM_7_RAD_VERT, random7rad, 0.1));
    }

    private static final Vector LO = new Vector(-1.1, -2.2, -1.1);
    private static final Vector HI = new Vector(0.9, 0, -0.9);
    private static final Set<Vector> BETWEEN = Stream.of(
            new Vector(-1, -2, -1),
            new Vector(0, -2, -1),
            new Vector(-1, -1, -1),
            new Vector(0, -1, -1),
            new Vector(-1, 0, -1),
            new Vector(0, 0, -1)
    ).collect(Collectors.toSet());

    @Test
    public void testBetween() {
        final Set<Vector> between = new HashSet<Vector>();
        for (final Vector v : Vector.between(LO, HI)) {
            between.add(v);
        }
        assertTrue(BETWEEN.equals(between));
    }

    private static final Vector CENTER = new Vector(1.1, 1.1, 1.1);
    private static final double RADIUS = 0.92;
    private static final Set<Vector> AROUND = Stream.of(
            new Vector(1, 1, 1),
            new Vector(2, 1, 1),
            new Vector(1, 2, 1),
            new Vector(1, 1, 2)
    ).collect(Collectors.toSet());

    @Test
    public void testAround() {
        final Set<Vector> around = new HashSet<Vector>();
        for (final Vector v : Vector.around(CENTER, RADIUS)) {
            around.add(v);
        }
        assertTrue(AROUND.equals(around));
    }

    // ##### isInt() #####

    private static final Vector NOT_INT_DECIMAL = new Vector(1, 1.0000001, 1);
    private static final Vector NOT_INT_INF = new Vector(1, Double.POSITIVE_INFINITY, 1);
    private static final Vector NOT_INT_NAN = new Vector(1, Double.NaN, 1);
    private static final Vector INT = new Vector(-1, 0, 1);

    @Test
    public void testIsInt() {
        assertFalse(NOT_INT_DECIMAL.isInt());
        assertFalse(NOT_INT_INF.isInt());
        assertFalse(NOT_INT_NAN.isInt());
        assertTrue(INT.isInt());
    }
}
