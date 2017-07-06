package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.Worker;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.utilities.PositionUtilities;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;
import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.blocks.Block;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simulator, which simulates the effects of physics.
 */
public class Simulator extends Worker {
    /**
     * The probability that a simulator scans a chunk for active objects on a given tick.
     */
    private static final double SCAN_PROBABILITY = 1 / 10;

    /**
     * The number of chunks in each direction from the player's chunk that a simulator will simulate the effects of
     * physics in.
     */
    private static final int SIMULATION_DISTANCE = Renderer.RENDER_DISTANCE;

    /**
     * A simulator's minimum tick interval. In nanoseconds.
     */
    private static final int MIN_INTERVAL = 1_000_000_000 / 60;

    /**
     * The world in which this simulator should simulate the effects of physics.
     */
    private final World world;

    /**
     * The world objects upon which this simulator is currently simulating the effects of physics.
     *
     * A map from positions of chunk anchor points to lists of the active objects in those chunks.
     */
    private final Map<List<Integer>, Set<WorldObject>> activeObjects;

    /**
     * Create a simulator.
     *
     * @param world A value for {@link #world}.
     */
    public Simulator(final World world) {
        this.world = world;
        this.activeObjects = new HashMap<>();
    }

    @Override
    protected long getInitialMinInterval() {
        return Simulator.MIN_INTERVAL;
    }

    // TODO: Throughout the codebase, use {@inheritDoc} only to add to a superclass's documentation, per https://goo.gl/QiFNLn.

    @Override
    protected void initInThread() {
        return;
    }

    @Override
    protected void tick() {
        final int[] playerChunkPos = PositionUtilities.toChunkPosition(this.world.getPlayer().getPosition());
        for (final int[] chunkPos : PositionUtilities.getNearbyChunkPositions(playerChunkPos,
                Simulator.SIMULATION_DISTANCE)) {
            if (ThreadLocalRandom.current().nextDouble() < Simulator.SCAN_PROBABILITY) {
                this.scanChunk(chunkPos);
            }
            final List<Integer> chunkPosList = ArrayUtilities.toList(chunkPos);
            final Set<WorldObject> activeObjectsChunk = this.activeObjects.get(chunkPosList);
            for (final WorldObject obj : activeObjectsChunk) {
                for (final Block block : this.getIntersectingBlocks(obj)) {
                    final double[] mtv = SeparatingAxisSolver.calcMTV(obj, block);
                    if (mtv != null) {
                        obj.changePosition(mtv);
                    }
                }
            }
        }
    }

    /**
     * Scan a chunk for new active objects.
     * @param pos The position of the anchor point of the chunk.
     */
    private void scanChunk(final int[] pos) {
        final List<Integer> posList = ArrayUtilities.toList(pos);
        if (!this.activeObjects.containsKey(posList)) {
            this.activeObjects.put(posList, new HashSet<>());
        }
        final Set<WorldObject> chunkEntities = this.activeObjects.get(posList);
        for (final Entity entity : this.world.getChunk(pos).getEntites()) {
            chunkEntities.add(entity);
        }
    }

    /**
     * Get the blocks which intersect a particular world object.
     * @param obj The world object.
     * @return The blocks which intersect the world object.
     */
    private Iterable<Block> getIntersectingBlocks(final WorldObject obj) {
        final double[] pos = obj.getPosition();
        final double[] hitBoxDims = obj.getHitBoxDims();
        final double[][] hitBoxVertices = new double[][]{
                new double[]{pos[0]                , pos[1]                , pos[2]                },
                new double[]{pos[0]                , pos[1]                , pos[2] + hitBoxDims[2]},
                new double[]{pos[0]                , pos[1] + hitBoxDims[1], pos[2]                },
                new double[]{pos[0]                , pos[1] + hitBoxDims[1], pos[2] + hitBoxDims[2]},
                new double[]{pos[0] + hitBoxDims[0], pos[1]                , pos[2]                },
                new double[]{pos[0] + hitBoxDims[0], pos[1]                , pos[2] + hitBoxDims[2]},
                new double[]{pos[0] + hitBoxDims[0], pos[1] + hitBoxDims[1], pos[2]                },
                new double[]{pos[0] + hitBoxDims[0], pos[1] + hitBoxDims[1], pos[2] + hitBoxDims[2]}
        };
        final int[] minHitBoxBlockCoords = PositionUtilities.toBlockPosition(ArrayUtilities.min(hitBoxVertices));
        final int[] maxHitBoxBlockCoords = PositionUtilities.toBlockPosition(ArrayUtilities.max(hitBoxVertices));
        final List<Block> intersectingBlocks = new LinkedList<>();
        final int[] blockPos = new int[3];
        for (blockPos[0] = minHitBoxBlockCoords[0]; blockPos[0] <= maxHitBoxBlockCoords[0]; blockPos[0]++) {
            for (blockPos[1] = minHitBoxBlockCoords[1]; blockPos[1] <= maxHitBoxBlockCoords[1]; blockPos[1]++) {
                for (blockPos[2] = minHitBoxBlockCoords[2]; blockPos[2] <= maxHitBoxBlockCoords[2]; blockPos[2]++) {
                    intersectingBlocks.add(this.world.getBlock(blockPos));
                }
            }
        }
        return intersectingBlocks;
    }
}
