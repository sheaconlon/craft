package com.sheaconlon.realcraft.entity.entities;

import com.sheaconlon.realcraft.entity.CompositeEntity;
import com.sheaconlon.realcraft.entity.Entity;
import com.sheaconlon.realcraft.physics.BoundingBox;
import com.sheaconlon.realcraft.positioning.Position;
import com.sheaconlon.realcraft.renderer.Quad;

import java.util.LinkedList;

/**
 * The player.
 */
public class Player extends CompositeEntity {
    /**
     * The bounding box of the head of a player.
     */
    private static final BoundingBox HEAD_BOUNDING_BOX = new BoundingBox(1,1, 1);

    /**
     * The bounding box of the body of a player.
     */
    private static final BoundingBox BODY_BOUNDING_BOX = new BoundingBox(1,2, 1);

    /**
     * The bounding box of a player.
     */
    private static final BoundingBox BOUNDING_BOX = new BoundingBox(1, 2, 1);

    /**
     * The player's head.
     */
    private class PlayerHead extends Entity {
        private final BoundingBox BOUNDING_BOX = new BoundingBox(1, 1, 1);

        /**
         * Construct a player head.
         * @param pos See {@link Entity#pos}.
         * @param xAngle See {@link Entity#xAngle}.
         * @param yAngle See {@link Entity#xAngle}.
         * @param zAngle See {@link Entity#xAngle}.
         */
        PlayerHead(final Position pos, final double xAngle, final double yAngle, final double zAngle) {
            super(pos, xAngle, yAngle, zAngle);
        }

        /**
         * {@inheritDoc}
         */
        public Iterable<Quad> getQuads() {
            return new LinkedList<>();
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
     * The player's body.
     */
    private class PlayerBody extends Entity {
        private final BoundingBox BOUNDING_BOX = new BoundingBox(1,2, 1);

        /**
         * Construct a player body.
         * @param pos See {@link Entity#pos}.
         * @param xAngle See {@link Entity#xAngle}.
         * @param yAngle See {@link Entity#xAngle}.
         * @param zAngle See {@link Entity#xAngle}.
         */
        PlayerBody(final Position pos,
                   final double xAngle, final double yAngle, final double zAngle) {
            super(pos, xAngle, yAngle, zAngle);
        }

        /**
         * {@inheritDoc}
         */
        public Iterable<Quad> getQuads() {
            return new LinkedList<>();
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
     * @param pos See {@link Entity#pos}.
     * @param xAngle See {@link Entity#xAngle}.
     * @param yAngle See {@link Entity#xAngle}.
     * @param zAngle See {@link Entity#xAngle}.
     * @param headXAngle The {@code xAngle} of the player's head relative to the {@code xAngle} of the player.
     * @param headYAngle The {@code yAngle} of the player's head relative to the {@code yAngle} of the player.
     * @param headZAngle The {@code zAngle} of the player's head relative to the {@code zAngle} of the player.
     */
    public Player(final Position pos, final double xAngle, final double yAngle, final double zAngle,
                  final double headXAngle, final double headYAngle, final double headZAngle) {
        super(pos, xAngle, yAngle, zAngle);
        this.body = new PlayerBody(pos, xAngle, yAngle, zAngle);
        this.addConstituent(this.body);
        pos.changeY(Player.HEAD_DELTA_Y);
        this.head = new PlayerHead(pos, xAngle + headXAngle, yAngle + headYAngle,
                zAngle + headZAngle);
        this.addConstituent(this.head);
    }
}
