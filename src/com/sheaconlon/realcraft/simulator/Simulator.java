package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.simulator.forces.AirResistance;
import com.sheaconlon.realcraft.simulator.forces.Force;
import com.sheaconlon.realcraft.simulator.forces.Gravity;
import com.sheaconlon.realcraft.world.Chunk;
import com.sheaconlon.realcraft.world.WorldObject;
import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A simulator of the laws of physics, environmental conditions, object updates, etc.
 */
public class Simulator extends Worker {
    public static final int SIMULATION_DISTANCE = 2;

    private static final double TARGET_FREQ = 60;
    private static final String NAME = "Simulator";
    private static final boolean NEEDS_DEDICATED_THREAD = false;
    private static final boolean NEEDS_MAIN_THREAD = false;
    private static final PRIORITY_LEVEL PRIORITY_LEVEL = Worker.PRIORITY_LEVEL.HIGH;
    private static final double SCAN_PROBABILITY = 0.25;
    private static final Force[] FORCES = new Force[]{
            new Gravity(),
            new AirResistance()
    };

    private final World world;
    private final List<Block> activeBlocks;

    public Simulator(final World world) {
        this.world = world;
        this.activeBlocks = new LinkedList<>();
    }

    @Override
    public double getTargetFreq() {
        return TARGET_FREQ;
    }

    @Override
    public String toString() {
        return NAME;
    }

    @Override
    public boolean needsDedicatedThread() {
        return NEEDS_DEDICATED_THREAD;
    }

    @Override
    public boolean needsMainThread() {
        return NEEDS_MAIN_THREAD;
    }

    @Override
    public PRIORITY_LEVEL getPriorityLevel() {
        return PRIORITY_LEVEL;
    }

    @Override
    public void tick(final double deltaT) {
        this.updateActiveBlocks(deltaT);
        this.scanInactiveBlocks(deltaT);
        this.updateEntities(deltaT);
    }

    private void updateActiveBlocks(final double deltaT) {
        for (final Iterator<Block> it = this.activeBlocks.iterator(); it.hasNext(); ) {
            final Block activeBlock = it.next();
            final boolean moving = this.update(activeBlock, deltaT);
            if (!moving) {
                it.remove();
            }
        }
    }

    private void scanInactiveBlocks(final double deltaT) {
        final List<Vector> nearbyChunkPositions =
                Chunk.getChunkPosNearby(Chunk.toChunkPos(this.world.getPlayer().getPos()), SIMULATION_DISTANCE);
        if (ThreadLocalRandom.current().nextDouble() < SCAN_PROBABILITY) {
            final Vector chosenChunkPos =
                    nearbyChunkPositions.get(ThreadLocalRandom.current().nextInt(nearbyChunkPositions.size()));
            final Chunk chosenChunk = this.world.getChunk(chosenChunkPos);
            if (chosenChunk == null) {
                return;
            }
            // TODO: Choose a chunk randomly from the set of loaded chunks.
            for (final Block block : chosenChunk.getBlocks()) {
                final boolean moving = this.update(block, deltaT);
                if (moving) {
                    this.activeBlocks.add(block);
                }
            }
        }
    }

    private void updateEntities(final double deltaT) {
        final List<Vector> nearbyChunkPositions =
                Chunk.getChunkPosNearby(Chunk.toChunkPos(this.world.getPlayer().getPos()), SIMULATION_DISTANCE);
        for (final Vector chunkPos: nearbyChunkPositions) {
            final Chunk chunk = this.world.getChunk(chunkPos);
            if (chunk == null) {
                continue;
            }
            for (final Entity e : chunk.getEntities()) {
                this.update(e, deltaT);
            }
        }
    }

    private boolean update(final WorldObject obj, final double deltaT) {
        obj.tick(this.world);
        this.updatePosition(obj, deltaT);
        return this.updateVelocity(obj, deltaT);
    }

    private void updatePosition(final WorldObject obj, final double deltaT) {
        obj.changePos(Vector.scale(obj.getVelocity(), deltaT));
        final Vector velocity = obj.getVelocity();
        for (final Hitbox objHitbox : obj.getHitboxes()) {
            final Vector[] objHitboxBounds = objHitbox.getBounds();
            objHitboxBounds[0] = Vector.subtract(objHitboxBounds[0], new Vector(-0.5, -0.5, -0.5));
            objHitboxBounds[1] = Vector.subtract(objHitboxBounds[1], new Vector(-0.5, -0.5, -0.5));
            for (int x = objHitboxBounds[0].getXInt(); x <= objHitboxBounds[1].getXInt(); x++) {
                for (int y = objHitboxBounds[0].getYInt(); y <= objHitboxBounds[1].getYInt(); y++) {
                    for (int z = objHitboxBounds[0].getZInt(); z <= objHitboxBounds[1].getZInt(); z++) {
                        final Vector blockPos = new Vector(x, y, z);
                        final Block block = this.world.getBlock(blockPos);
                        for (final Hitbox blockHitbox : block.getHitboxes()) {
                            final Vector minTrans = objHitbox.minTranslation(blockHitbox);
                            obj.changePos(minTrans);
                            final double mag = minTrans.mag();
                            if (mag != 0) {
                                obj.changeVelocity(
                                        Vector.multiply(obj.getVelocity(),
                                                Vector.scale(minTrans, -1 / minTrans.mag())
                                        )
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean updateVelocity(final WorldObject obj, final double deltaT) {
        Vector netForce = Vector.ZERO_VECTOR;
        for (final Force f : FORCES) {
            netForce = Vector.add(netForce, f.apply(obj, this.world));
        }
        if (netForce.isZero()) {
            return false;
        }
        final Vector acceleration = Vector.scale(netForce, 1 / obj.getMass());
        obj.changeVelocity(Vector.scale(acceleration, deltaT));
        return true;
    }
}
