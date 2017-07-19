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

    private static final float[] toFloatArray(final double[] arr) {
        final float[] floatArr = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            floatArr[i] = (float)arr[i];
        }
        return floatArr;
    }

    private static float[] add(final float[] a, final float[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("cannot add arrays of unequal length");
        }
        final float[] sum = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            sum[i] = a[i] + b[i];
        }
        return sum;
    }

    /**
     * @return The element-wise sum of {@code a} and {@code b}. A new array.
     */
    public static float[] add(final float[] a, final double[] b) {
        return add(a, toFloatArray(b));
    }
}
