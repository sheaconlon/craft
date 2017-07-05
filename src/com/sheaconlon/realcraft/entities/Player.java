package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.world.WorldObject;

/**
 * The player, the avatar of the user.
 */
public class Player extends Animal {
    /**
     * A player's return value for {@link #getHitBoxDims()}.
     */
    private static final double[] HIT_BOX_DIMS = new double[]{1, 2, 1};

    /**
     * The initial velocity of a player.
     */
    private static final double[] INITIAL_VELOCITY = new double[]{0, 0, 0};

    /**
     * Create a player.
     * @param position The position of the player.
     * @param xzOrientation See {@link WorldObject#xzOrientation}.
     * @param xzCrossOrientation See {@link WorldObject#xzCrossOrientation}.
     */
    public Player(final double[] position, final double xzOrientation, final double xzCrossOrientation) {
        super(position, Player.INITIAL_VELOCITY, xzOrientation, xzCrossOrientation);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][][] getVertexData() {
        // TODO: Make vertex data for the player.
        throw new UnsupportedOperationException();
    }

    @Override
    public double[] getHitBoxDims() {
        return Player.HIT_BOX_DIMS;
    }
}
