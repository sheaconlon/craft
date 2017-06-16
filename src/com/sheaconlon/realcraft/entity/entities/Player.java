package com.sheaconlon.realcraft.entity.entities;

import com.sheaconlon.realcraft.entity.CompositeEntity;
import com.sheaconlon.realcraft.entity.Entity;
import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.physics.Physical;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.LinkedList;
import java.util.Iterator;

/**
 * The player, the avatar of the user.
 */
public class Player extends CompositeEntity {
    /**
     * The quads of a player's head.
     */
    private static final Iterable<Quad> PLAYER_HEAD_QUADS = new LinkedList<Quad>();

    /**
     * The quads of a player's body.
     */
    private static final Iterable<Quad> PLAYER_BODY_QUADS = new LinkedList<Quad>();

    /**
     * The bounding box of the head of a player.
     */
    private static final BoundingBox HEAD_BOUNDING_BOX = new BoundingBox(1,1, 1);

    /**
     * The bounding box of the body of a player.
     */
    private static final BoundingBox BODY_BOUNDING_BOX = new BoundingBox(1,2, 1);

    /**
     * A player's head.
     */
    private class PlayerHead extends Entity {
        /**
         * @see Physical#Physical(Position)
         */
        PlayerHead(final Position position) {
            super(position);
        }

        /**
         * {@inheritDoc}
         */
        public Iterator<Quad> iterator() {
            // TODO: Add quads for PlayerHead.
            return Player.PLAYER_HEAD_QUADS.iterator();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BoundingBox getBoundingBox() {
            return Player.HEAD_BOUNDING_BOX;
        }
    }

    /**
     * A player's body.
     */
    private class PlayerBody extends Entity {
        /**
         * {@inheritDoc}
         */
        public Iterator<Quad> iterator() {
            // TODO: Add quads for PlayerBody.
            return Player.PLAYER_BODY_QUADS.iterator();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public BoundingBox getBoundingBox() {
            return Player.BODY_BOUNDING_BOX;
        }
    }

    /**
     * The difference between the y-coordinates of the player's head and the player's body, in feet.
     */
    private static final double HEAD_HEIGHT = 1.25;

    private static final Position EYE_POSITION = new Position(0.5, Player.HEAD_HEIGHT, 0.5);

    /**
     * The player's head.
     */
    private final PlayerHead head;

    /**
     * The player's body.
     */
    private final PlayerBody body;

    /**
     * @see Physical#Physical{Position)
     */
    public Player(final Position position) {
        super(position);
        this.body = new PlayerBody();
        this.addConstituent(this.body);
        final Position headPosition = new Position(position);
        headPosition.changeY(Player.HEAD_HEIGHT);
        this.head = new PlayerHead(headPosition);
        this.addConstituent(this.head);
    }

    public Position getEyePosition() {
        return Player.EYE_POSITION;
    }
}
