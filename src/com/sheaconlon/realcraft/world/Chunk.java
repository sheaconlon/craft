package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.utilities.Vector;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.List;
import java.util.LinkedList;

/**
 * A cubical subset of the world.
 */
public class Chunk {
    private class BlockIterator implements Iterator<Block> {
        private Vector curr;

        private BlockIterator() {
            this.curr = Vector.ZERO_VECTOR;
        }

        @Override
        public boolean hasNext() {
            return this.curr.getX() < Chunk.SIZE && this.curr.getY() < Chunk.SIZE && this.curr.getZ() < Chunk.SIZE;
        }

        @Override
        public Block next() {
            final Block next = Chunk.this.blocks[(int)this.curr.getX()][(int)this.curr.getY()][(int)this.curr.getZ()];
            this.curr = new Vector(this.curr.getX() + 1, this.curr.getY(), this.curr.getZ());
            if (this.curr.getX() >= Chunk.SIZE) {
                this.curr = new Vector(0, this.curr.getY() + 1, this.curr.getZ());
            }
            if (this.curr.getY() >= Chunk.SIZE) {
                this.curr = new Vector(0, 0, this.curr.getZ() + 1);
            }
            return next;
        }
    }

    private class Blocks implements Iterable<Block> {
        @Override
        public Iterator<Block> iterator() {
            return new BlockIterator();
        }
    }

    /**
     * The side length of a chunk, in blocks.
     */
    public static final int SIZE = 15;

    /**
     * The blocks in this chunk.
     */
    private final Block[][][] blocks;

    /**
     * The entities in this chunk.
     */
    private final List<Entity> entities;

    /**
     * The position of the anchor point of this chunk.
     */
    private final Vector position;

    /**
     * Create a chunk.
     * @param position The anchor point of the chunk.
     */
    public Chunk(final Vector position) {
        this.blocks = new Block[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
        this.entities = new LinkedList<>();
        this.position = position;
        final int[] blockPosition = new int[]{0, 0, 0};
        for (blockPosition[0] = 0; blockPosition[0] < Chunk.SIZE; blockPosition[0]++) {
            for (blockPosition[1] = 0; blockPosition[1] < Chunk.SIZE; blockPosition[1]++) {
                for (blockPosition[2] = 0; blockPosition[2] < Chunk.SIZE; blockPosition[2]++) {
                    final Vector pos = Vector.add(this.position, new Vector(blockPosition[0], blockPosition[1], blockPosition[2]));
                    this.blocks[blockPosition[0]][blockPosition[1]][blockPosition[2]] =
                            new AirBlock(pos);
                }
            }
        }
    }

    /**
     * Put a block in a particular position.
     * @param blockPosition The position.
     * @param block The block.
     */
    public void putBlock(final Vector blockPosition, final Block block) {
        final Vector relativeBlockPosition = Vector.subtract(blockPosition, this.position);
        this.blocks[(int)relativeBlockPosition.getX()][(int)relativeBlockPosition.getY()]
                [(int)relativeBlockPosition.getZ()] = block;
    }

    /**
     * Add an entity to this chunk.
     * @param entity The entity.
     */
    public void addEntity(final Entity entity) {
        this.entities.add(entity);
    }

    /**
     * Remove an entity from this chunk.
     * @param entity The entity.
     */
    public void removeEntity(final Entity entity) {
        this.entities.remove(entity);
    }

    /**
     * Get the entities in this chunk.
     * @return The entities in this chunk.
     */
    public Iterable<Entity> getEntities() {
        return this.entities;
    }

    /**
     * Get a block at some position.
     * @param pos The position.
     * @return The block at {@code pos}.
     */
    public Block getBlock(final Vector pos) {
        final Vector relativePosition = Vector.subtract(pos, this.position);
        return this.blocks[(int)relativePosition.getX()][(int)relativePosition.getY()][(int)relativePosition.getZ()];
    }

    /**
     * Get the blocks in this chunk.
     * @return The blocks in this chunk.
     */
    public Iterable<Block> getBlocks() {
        return new Blocks();
    }

    /**
     * Return the position of the anchor point of the chunk containing some position.
     * @param pos The position.
     * @return The position of the anchor point of the chunk containing {@code pos}.
     */
    public static Vector toChunkPos(final Vector pos) {
        return Vector.scale(Vector.round(Vector.scale(pos, 1.0 / Chunk.SIZE)), Chunk.SIZE);
    }

    /**
     * Get the chunk positions which are "nearby" some chunk position.
     * @param pos The chunk position.
     * @param distance The maximum number of chunks away at which a chunk position will be considered nearby
     * {@code pos}.
     * @return The chunk positions which are "nearby" {@code pos}.
     */
    public static List<Vector> getChunkPosNearby(final Vector pos, final int distance) {
        final Iterable<Vector> nearbyDisplacements = Vector.getNearby(Vector.ZERO_VECTOR, distance);
        final List<Vector> nearbyChunkPos = new LinkedList<>();
        for (final Vector disp : nearbyDisplacements) {
            nearbyChunkPos.add(Vector.add(Vector.scale(disp, Chunk.SIZE), pos));
        }
        return nearbyChunkPos;
    }
}
