package com.sheaconlon.realcraft.generator;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * A Perlin noise generator.
 *
 * Implemented using the guide at http://flafla2.github.io/2014/08/09/perlinnoise.html.
 */
public class PerlinNoiseGenerator {
    // ##### PRIVATE STATIC FINAL #####
    private static final UnaryOperator<Double> EASING_FUNCTION = new UnaryOperator<Double>() {
        @Override
        public Double apply(Double x) {
            final double x3 = x * x * x;
            final double x4 = x3 * x;
            final double x5 = x4 * x;
            return 6*x5 - 15*x4 + 10*x3;
        }
    };

    // ##### PRIVATE FINAL #####
    private final double frequency;
    private final double amplitude;
    private final UnaryOperator<Double> postTransformation;

    // ##### CONSTRUCTORS #####
    /**
     * Create a Perlin noise generator.
     * @param frequency The frequency of the noise. Higher values yield more rapidly varying noise.
     * @param amplitude The amplitude of the noise. Higher values yield noise over a larger range.
     * @param postTransformation The postprocessing transformation to apply to the noise.
     */
    public PerlinNoiseGenerator(final double frequency, final double amplitude,
                                final UnaryOperator<Double> postTransformation) {
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.postTransformation = postTransformation;
    }

    // ##### PUBLIC #####
    /**
     * Get the value of the Perlin noise at some point.
     * @param point The point.
     * @return The value of the Perlin noise at {@code point}.
     */
    public double noise(final Vector point) {
        final Vector scaledPoint = Vector.scale(point, this.frequency);
        final Vector lowVertex = Vector.round(scaledPoint);
        List<Double> influences = new ArrayList<>();
        for (final Vector unitCubeVertex : Vector.UNIT_CUBE_VERTICES) {
            final Vector gridPoint = Vector.add(lowVertex, unitCubeVertex);
            final Vector gradient = gradient(gridPoint);
            final Vector dispToGridPoint = Vector.subtract(gridPoint, scaledPoint);
            final double influence = Vector.multiply(dispToGridPoint, gradient).sum();
            influences.add(influence);
        }
        List<Double> interpolatedInfluences = new ArrayList<>();
        final Vector relativeInput = Vector.apply(Vector.subtract(scaledPoint, lowVertex), EASING_FUNCTION);
        for (int i = 0; i < influences.size(); i += 2) {
            interpolatedInfluences.add(lerp(influences.get(i), influences.get(i + 1), relativeInput.getX()));
        }
        influences = interpolatedInfluences;
        interpolatedInfluences = new ArrayList<>();
        for (int i = 0; i < influences.size(); i += 2) {
            interpolatedInfluences.add(lerp(influences.get(i), influences.get(i + 1), relativeInput.getY()));
        }
        influences = interpolatedInfluences;
        interpolatedInfluences = new ArrayList<>();
        for (int i = 0; i < influences.size(); i += 2) {
            interpolatedInfluences.add(lerp(influences.get(i), influences.get(i + 1), relativeInput.getZ()));
        }
        return this.postTransformation.apply(interpolatedInfluences.get(0) * this.amplitude);
    }

    // ##### PRIVATE #####
    private static Vector gradient(final Vector gridPoint) {
        final int random = (int)(gridPoint.hashCode() / Double.MAX_VALUE * Vector.UNIT_CUBE_VERTICES.size());
        return Vector.UNIT_CUBE_VERTICES.get(random);
    }

    private static double lerp(final double a, final double b, final double x) {
        return a + x * (b - a);
    }
}
