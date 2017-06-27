package com.sheaconlon.realcraft.entity.entities;

import com.sheaconlon.realcraft.entity.Animal;

/**
 * The player, the avatar of the user.
 */
public class Player extends Animal {
    /**
     * The initial velocity of a player.
     */
    private static final double[] INITIAL_VELOCITY = new double[]{0, 0, 0};

    /**
     * Create a player.
     * @param position The position of the player.
     * @param orientation See {@link Animal#orientation}.
     * @param lookDirection See {@link Animal#lookDirection}.
     */
    public Player(final double[] position, final double orientation, final double lookDirection) {
        super(position, Player.INITIAL_VELOCITY, orientation, lookDirection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][][] getVertexData() {
        // TODO: Make vertex data for the player.
        throw new UnsupportedOperationException();
    }
}
