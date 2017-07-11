package com.sheaconlon.realcraft.utilities;

/**
 * Some utility functions for arrays.
 */
public class ArrayUtilities {
    /**
     * Get the maximum value in an array.
     * @param arr The array.
     * @return The maximum value in {@code arr}.
     */
    public static long max(final long[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("cannot get maximum of length-0 array");
        }
        long max = arr[0]; // safe because exception has been thrown if arr has length 0
        for (long elem : arr) {
            max = Math.max(max, elem);
        }
        return max;
    }
}
