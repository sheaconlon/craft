package com.sheaconlon.realcraft.world;

import com.sheaconlon.realcraft.blocks.AirBlock;
import com.sheaconlon.realcraft.blocks.Block;
import com.sheaconlon.realcraft.utilities.ArrayUtilities;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A cubical subset of the world.
 */
public class Chunk extends Container {
    private class ChunkContentsIterator implements Iterator<WorldObject> {
        /**
         * The x-coordinate of the current block, relative to the anchor point of this chunk.
         */
        private int x;

        /**
         * The y-coordinate of the current block, relative to the anchor point of this chunk.
         */
        private int y;

        /**
         * The z-coordinate of the current block, relative to the anchor point of this chunk.
         */
        private int z;

        /**
         * Whether this chunk contents iterator has a next world object.
         */
        private boolean hasNext;

        ChunkContentsIterator() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
            this.hasNext = this.x < Chunk.SIZE && this.y < Chunk.SIZE && this.z < Chunk.SIZE;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            return this.hasNext;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WorldObject next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final WorldObject result = Chunk.this.blocks[this.x][this.y][this.z];
            this.x++;
            if (this.x >= Chunk.SIZE) {
                this.x = 0;
                this.y++;
            }
            if (this.y >= Chunk.SIZE) {
                this.y = 0;
                this.z++;
            }
            if (this.z >= Chunk.SIZE) {
                this.hasNext = false;
            }
            return result;
        }
    }

    /**
     * The side length of a chunk, in blocks.
     */
    public static final int SIZE = 50;

    /**
     * The blocks in this chunk.
     */
    private final Block[][][] blocks;

    /**
     * An integer version of {@link Container#position}.
     */
    private final int[] position;

    /**
     * Create a chunk.
     * @param position The anchor point of the chunk.
     */
    public Chunk(final int[] position) {
        super(null, ArrayUtilities.toDoubleArray(position));
        this.blocks = new Block[Chunk.SIZE][Chunk.SIZE][Chunk.SIZE];
        this.position = ArrayUtilities.copy(position);
        final int[] blockPosition = new int[]{0, 0, 0};
        for (blockPosition[0] = 0; blockPosition[0] < Chunk.SIZE; blockPosition[0]++) {
            for (blockPosition[1] = 0; blockPosition[1] < Chunk.SIZE; blockPosition[1]++) {
                for (blockPosition[2] = 0; blockPosition[2] < Chunk.SIZE; blockPosition[2]++) {
                    this.blocks[blockPosition[0]][blockPosition[1]][blockPosition[2]] =
                            new AirBlock(this, blockPosition);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<WorldObject> getContents() {
        return new ChunkContentsIterator();
    }

    /**
     * Put a block in a particular position.
     * @param blockPosition The position.
     * @param block The block.
     */
    public void putBlock(final int[] blockPosition, final Block block) {
        this.blocks[blockPosition[0] - this.position[0]][blockPosition[1] - this.position[1]]
                [blockPosition[2] - this.position[2]] = block;
    }
}
