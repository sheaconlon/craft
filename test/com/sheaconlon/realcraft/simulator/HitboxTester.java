package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.utilities.Vector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A tester of hitboxes.
 */
class HitboxTester {
    private class DummyEntity extends Entity {
        public DummyEntity(Vector pos, double orient, Vector velocity) {
            super(pos, orient, velocity);
        }

        @Override
        public float[][][] getVertexData() {
            return new float[0][][];
        }

        @Override
        public double getMass() {
            return 0;
        }

        @Override
        public double getCompressiveStrength() {
            return 0;
        }

        @Override
        public List<Hitbox> getHitboxes() {
            return null;
        }
    }

    @Test
    void testGetBounds() {
        final Entity dummyEntity = new DummyEntity(new Vector(1.5, 1.5, 1.5), 0, Vector.ZERO_VECTOR);
        final Hitbox hitbox = new Hitbox(dummyEntity, new Vector(-0.5, -0.5, -0.5), new Vector(1, 1, 1));
        dummyEntity.setOrient(Math.PI / 4);
        final Vector[] bounds = hitbox.getBounds();
        assertEquals(1.5 - Math.sqrt(2)/2, bounds[0].getX(), 0.01);
        assertEquals(1.5 - Math.sqrt(2)/2, bounds[0].getZ(), 0.01);
        assertEquals(1, bounds[0].getY());
        assertEquals(1.5 + Math.sqrt(2)/2, bounds[1].getX(), 0.01);
        assertEquals(1.5 + Math.sqrt(2)/2, bounds[1].getZ(), 0.01);
        assertEquals(2, bounds[1].getY());
    }
}
