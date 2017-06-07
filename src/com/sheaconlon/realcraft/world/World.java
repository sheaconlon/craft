package com.sheaconlon.realcraft.world;

import java.util.HashMap;
import java.util.Map;

/**
 * The world.
 */
public class World {
    /**
     * The chunks of the world.
     */
    private final Map<int[], Chunk> chunks;

    /**
     * Construct a world.
     */
    public World() {
        this.chunks = new HashMap<>();
    }
}
