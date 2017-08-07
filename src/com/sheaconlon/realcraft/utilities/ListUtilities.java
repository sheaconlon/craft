package com.sheaconlon.realcraft.utilities;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Some utility functions for arrays.
 */
public class ListUtilities {
    /**
     * Create an unmodifiable list containing some elements.
     * @param elements The elements.
     * @param <T> The type of the elements.
     * @return An unmodifiable list of {@code elements}.
     */
    public static <T> List<T> unmodifiableList(final T... elements) {
        return Collections.unmodifiableList(Arrays.asList(elements));
    }
}
