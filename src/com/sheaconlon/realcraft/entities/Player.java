package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.renderer.Quad;

import java.util.LinkedList;

/**
 * The player.
 */
public class Player extends CompositeEntity {
    /**
     * The player's head.
     */
    private class PlayerHead extends Entity {
        /**
         * Construct a player head.
         * @param x See {@link Entity#x}.
         * @param y See {@link Entity#y}.
         * @param z See {@link Entity#z}.
         * @param xAngle See {@link Entity#xAngle}.
         * @param yAngle See {@link Entity#xAngle}.
         * @param zAngle See {@link Entity#xAngle}.
         */
        PlayerHead(final double x, final double y, final double z,
                   final double xAngle, final double yAngle, final double zAngle) {
            super(x, y, z, xAngle, yAngle, zAngle);
        }

        /**
         * {@inheritDoc}
         */
        public Iterable<Quad> getQuads() {
            return new LinkedList<>();
        }
    }

    /**
     * The player's body.
     */
    private class PlayerBody extends Entity {
        /**
         * Construct a player body.
         * @param x See {@link Entity#x}.
         * @param y See {@link Entity#y}.
         * @param z See {@link Entity#z}.
         * @param xAngle See {@link Entity#xAngle}.
         * @param yAngle See {@link Entity#xAngle}.
         * @param zAngle See {@link Entity#xAngle}.
         */
        PlayerBody(final double x, final double y, final double z,
                   final double xAngle, final double yAngle, final double zAngle) {
            super(x, y, z, xAngle, yAngle, zAngle);
        }

        /**
         * {@inheritDoc}
         */
        public Iterable<Quad> getQuads() {
            return new LinkedList<>();
        }
    }

    /**
     * The differences between the y-coordinates of the anchor points of the player's head and the player's body.
     */
    private static final double HEAD_DELTA_Y = 1.25;

    /**
     * The player's head.
     */
    private final PlayerHead head;

    /**
     * The player's body.
     */
    private final PlayerBody body;

    /**
     * Construct a player.
     * @param x See {@link Entity#x}.
     * @param y See {@link Entity#y}.
     * @param z See {@link Entity#z}.
     * @param xAngle See {@link Entity#xAngle}.
     * @param yAngle See {@link Entity#xAngle}.
     * @param zAngle See {@link Entity#xAngle}.
     * @param headXAngle The {@code xAngle} of the player's head relative to the {@code xAngle} of the player.
     * @param headYAngle The {@code yAngle} of the player's head relative to the {@code yAngle} of the player.
     * @param headZAngle The {@code zAngle} of the player's head relative to the {@code zAngle} of the player.
     */
    public Player(final double x, final double y, final double z,
                  final double xAngle, final double yAngle, final double zAngle,
                  final double headXAngle, final double headYAngle, final double headZAngle) {
        super(x, y, z, xAngle, yAngle, zAngle);
        this.head = new PlayerHead(x, y, z, xAngle + headXAngle, yAngle + headYAngle, zAngle + headZAngle);
        this.addConstituent(this.head);
        this.body = new PlayerBody(x, y, z, xAngle, yAngle, zAngle);
        this.addConstituent(this.body);
    }
}
