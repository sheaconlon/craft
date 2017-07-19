package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The player, the avatar of the user.
 */
public class Player extends Animal {
    private static final double COMPRESSIVE_STRENGTH = 100;
    private static final double MASS = 100;
    private static final Vector[] HITBOX_POSITIONS = new Vector[]{
            new Vector(-0.3, 0, -0.3)
    };
    private static final Vector[] HITBOX_DIMS = new Vector[]{
            new Vector(0.6, 1.75, 0.6)
    };

    /**
     * The initial velocity of a player.
     */
    private static final Vector INITIAL_VELOCITY = new Vector(0, 0, 0);

    private final List<Hitbox> hitboxes;

    /**
     * Create a player.
     * @param pos See {@link WorldObject#getPos()}.
     * @param orient See {@link WorldObject#getOrient()}.
     * @param vertOrient See {@link Animal#getVertOrient()}.
     */
    public Player(final Vector pos, final double orient, final double vertOrient) {
        super(pos, Player.INITIAL_VELOCITY, orient, vertOrient);
        this.hitboxes = new LinkedList<>();
        for (int i = 0; i < HITBOX_POSITIONS.length; i++) {
            this.hitboxes.add(new Hitbox(this, HITBOX_POSITIONS[i], HITBOX_DIMS[i]));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float[][][] getVertexData() {
        return new float[0][][];
    }

    @Override
    public List<Hitbox> getHitboxes() {
        return Collections.unmodifiableList(this.hitboxes);
    }
}
