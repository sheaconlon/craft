package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

/**
 * The player, the avatar of the user.
 */
public class Player extends Animal {
    /**
     * A player's return value for {@link #getHitBoxDims()}.
     */
    public static final double[] HIT_BOX_DIMS = new double[]{0.8, 1.75, 0.8};

    /**
     * The initial velocity of a player.
     */
    private static final Vector INITIAL_VELOCITY = new Vector(0, 0, 0);

    /**
     * Create a player.
     * @param position The position of the player.
     * @param horizontalOrientation See {@link WorldObject#orient}.
     * @param verticalOrientation See {@link WorldObject#vertOrient}.
     */
    public Player(final Vector position, final double horizontalOrientation, final double verticalOrientation) {
        super(position, Player.INITIAL_VELOCITY, horizontalOrientation, verticalOrientation);
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
