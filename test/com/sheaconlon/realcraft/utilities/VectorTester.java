package com.sheaconlon.realcraft.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * A tester of vectors.
 */
public class VectorTester {
    private static final Vector POS_X = new Vector(1, 0, 0);
    private static final Vector POS_X_30 = new Vector(Math.sqrt(3) / 2, 0, -1.0/2.0);
    private static final Vector NEG_Z = new Vector(0, 0, -1);
    private static final Vector NEG_Z_45 = new Vector(-Math.sqrt(2) / 2, 0, -Math.sqrt(2) / 2);

    @Test
    public void testRotateHorizontal() {
        final Vector posX30 = Vector.rotateHorizontal(POS_X, -Math.PI / 6);
        assertVectorAboutEquals(POS_X_30, posX30);
        final Vector negZ45 = Vector.rotateHorizontal(NEG_Z, -Math.PI / 4);
        assertVectorAboutEquals(NEG_Z_45, negZ45);
    }

    private void assertVectorAboutEquals(final Vector expected, final Vector actual) {
        assertArrayEquals(expected.toArray(), actual.toArray(), 0.01);
    }
}
