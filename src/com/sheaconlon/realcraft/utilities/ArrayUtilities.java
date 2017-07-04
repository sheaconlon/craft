package com.sheaconlon.realcraft.utilities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * A collection of array utilities.
 */
public class ArrayUtilities {
    /**
     * Convert an integer array to a double array.
     * @param arr The integer array;
     * @return The double array.
     */
    public static double[] toDoubleArray(final int[] arr) {
        final double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert a double array to a float array.
     * @param arr The double array;
     * @return The float array.
     */
    public static float[] toFloatArray(final double[] arr) {
        final float[] result = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = (float)arr[i];
        }
        return result;
    }

    /**
     * Copy an int array.
     * @param arr The array.
     * @return A copy of the array.
     */
    public static int[] copy(final int[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }

    /**
     * Copy a float array.
     * @param arr The array.
     * @return A copy of the array.
     */
    public static float[] copy(final float[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }

    /**
     * Copy a double array.
     * @param arr The array.
     * @return A copy of the array.
     */
    public static double[] copy(final double[] arr) {
        return Arrays.copyOf(arr, arr.length);
    }

    /**
     * Add two integer arrays.
     * @param a The first array.
     * @param b The second array.
     * @return The array that is the elementwise sum of the input arrays.
     */
    public static int[] add(final int[] a, final int[] b) {
        final int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    /**
     * Add a float array and a double array.
     * @param a The float array.
     * @param b The double array.
     * @return The array that is the elementwise sum of the input arrays.
     */
    public static float[] add(final float[] a, final double[] b) {
        final float[] result = new float[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (float)(a[i] + b[i]);
        }
        return result;
    }

    /**
     * Subtract two integer arrays.
     * @param a The first array.
     * @param b The second array.
     * @return The array that is the elementwise difference of the input arrays ({@code a} - {@code b}).
     */
    public static int[] subtract(final int[] a, final int[] b) {
        final int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    /**
     * Multiply an integer array with a double array.
     * @param a The integer array.
     * @param b The double array.
     * @return The array that is the elementwise product of the input arrays.
     */
    public static double[] multiply(final int[] a, final double[] b) {
        final double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
        return result;
    }

    /**
     * Multiply an integer array by an integer factor.
     * @param arr The array.
     * @param c The factor.
     * @return The array multiplied by the factor.
     */
    public static int[] multiply(final int[] arr, final int c) {
        final int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] * c;
        }
        return result;
    }

    /**
     * Convert an integer array to a list of integers.
     * @param arr The array.
     * @return The list.
     */
    public static List<Integer> toList(final int[] arr) {
        final List<Integer> lst = new LinkedList<Integer>();
        for (final int x : arr) {
            lst.add(x);
        }
        return lst;
    }

    /**
     * Get the maximum of an array of longs.
     * @param arr The array, which must be of length at least 1.
     * @return The maximum.
     */
    public static long max(final long[] arr) {
        long max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(max, arr[i]);
        }
        return max;
    }
}
