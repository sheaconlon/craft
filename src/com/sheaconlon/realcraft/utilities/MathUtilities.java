package com.sheaconlon.realcraft.utilities;

/**
 * Some mathematical utility functions.
 */
public class MathUtilities {
    /**
     * Return the modulus of a number.
     *
     * The modulus, as implemented here, is never negative.
     * @param x The dividend.
     * @param y The divisor.
     * @return x mod y
     */
    public static int modulo(final int x, final int y) {
        return ((x % y) + y) % y;
    }
}
