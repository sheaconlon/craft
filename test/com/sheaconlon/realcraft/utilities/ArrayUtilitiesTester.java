package com.sheaconlon.realcraft.utilities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A tester of the array utilities.
 */
public class ArrayUtilitiesTester {

    private static final float[] A = new float[]{1.1f, 2.2f, 3.3f};
    private static final double[] B = new double[]{1.1, 2.2, 5.5};
    private static final float[] C = new float[]{2.2f, 4.4f, 8.8f};

    private static final float[] D = new float[]{10.1f};

    private static final float[] E = new float[]{};
    private static final double[] F = new double[]{};

    @Test
    public void testAddFloatFloat() {
        assertArrayEquals(C, ArrayUtilities.add(A, B), "typical case gives incorrect result");

        boolean thrown = false;
        try {
            ArrayUtilities.add(D, B);
        } catch (final IllegalArgumentException e) {
            thrown = true;
        }
        assertTrue(thrown, "IllegalArgumentException not thrown when lengths mismatched");

        assertArrayEquals(E, ArrayUtilities.add(E, F), "adding length-0 arrays does not result in length-0 array");
    }
}
