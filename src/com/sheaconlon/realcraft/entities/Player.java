package com.sheaconlon.realcraft.entities;

import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.simulator.Hitbox;
import com.sheaconlon.realcraft.utilities.Vector;
import com.sheaconlon.realcraft.world.WorldObject;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The player, the avatar of the user.
 */
public class Player extends Animal {
    // ##### PRIVATE STATIC FINAL #####
    private static final Vector INITIAL_POSITION = Vector.ZERO;
    private static final double INITIAL_ORIENTATION = 0;
    private static final Vector INITIAL_VELOCITY = Vector.ZERO;
    private static final double INITIAL_VERTICAL_ORIENTATION = 0;
    private static final List<Hitbox> HITBOXES = Collections.unmodifiableList(
            Stream.of(
                new Hitbox(
                        null,
                        new Vector(-0.3, 0, -0.3),
                        new Vector(0.6, 1.75, 0.6)
                )
            ).collect(
                    Collectors.toList()
            )
    );
    private static final List<Vertex> VERTICES = Collections.unmodifiableList(Collections.emptyList());

    // ##### PUBLIC STATIC FINAL #####
    /**
     * The player.
     */
    public static final Player PLAYER = new Player();

    // ##### CONSTRUCTORS #####
    private Player() {
        super(INITIAL_POSITION, INITIAL_VELOCITY, INITIAL_ORIENTATION, INITIAL_VERTICAL_ORIENTATION);
    }

    // ##### WORLD OBJECT OVERRIDES #####
    @Override
    public List<Vertex> getVertices() {
        return VERTICES;
    }

    @Override
    public List<Hitbox> getHitboxes() {
        return HITBOXES;
    }
}
