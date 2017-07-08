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
     * Subtract two arrays.
     * @param a An array.
     * @param b An array.
     * @return The array that is the elementwise difference {@code a} - {@code b}.
     */
    public static double[] subtract(final double[] a, final double[] b) {
        final double[] result = new double[a.length];
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
     * Get the absolute value of an array of integers.
     * @param arr The array.
     * @return The array that is the elementwise absolute value of the input array.
     */
    public static int[] abs(final int[] arr) {
        final int[] absArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            absArr[i] = Math.abs(arr[i]);
        }
        return absArr;
    }

    /**
     * Get the sum of an array of doubles.
     * @param arr The array, which must be of length at least 1.
     * @return The sum.
     */
    public static double sum(final double[] arr) {
        double sum = arr[0];
        for (int i = 1; i < arr.length; i++) {
            sum = sum + arr[i];
        }
        return sum;
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

    /**
     * Get the minimum of an array across the first dimension.
     * @param arr The array.
     * @return The minimum of the array across the first dimension.
     */
    public static double[] min(final double[][] arr) {
        final double[] min = new double[arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                min[j] = Math.min(min[j], arr[i][j]);
            }
        }
        return min;
    }

    /**
     * Get the maximum of an array across the first dimension.
     * @param arr The array.
     * @return The maximum of the array across the first dimension.
     */
    public static double[] max(final double[][] arr) {
        final double[] max = new double[arr[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                max[j] = Math.max(max[j], arr[i][j]);
            }
        }
        return max;
    }

    /**
     * Add two arrays elementwise.
     * @param a An array.
     * @param b An array.
     * @return The elementwise sum of {@code a} and {@code b}.
     */
    public static double[] add(final double[] a, final double[] b) {
        final double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    /**
     * Multiply two arrays elementwise.
     * @param a An array.
     * @param b An array.
     * @return The elementwise product of {@code a} and {@code b}.
     */
    public static double[] multiply(final double[] a, final double[] b) {
        final double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b[i];
        }
        return result;
    }

    /**
     * Multiply an array by a scalar.
     * @param arr The array.
     * @param c The scalar.
     * @return A new array that is {@code arr} multiplied by {@code c}.
     */
    public static double[] multiply(final double[] arr, final double c) {
        final double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] * c;
        }
        return result;
    }

    /**
     * Get the squared magnitude of a vector.
     * @param vec The vector.
     * @return The squared magnitude of {@code vec}.
     */
    public static double squaredMagnitude(final double[] vec) {
        double result = 0;
        for (final double x : vec) {
            result += x * x;
        }
        return result;
    }
}
