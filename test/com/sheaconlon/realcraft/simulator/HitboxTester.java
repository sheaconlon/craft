package com.sheaconlon.realcraft.simulator;

import com.sheaconlon.realcraft.entities.Entity;
import com.sheaconlon.realcraft.renderer.Vertex;
import com.sheaconlon.realcraft.utilities.Vector;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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
        public List<Vertex> getVertices() {
            return Collections.emptyList();
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

    @Test
    void testMinTranslation() {
        final Entity dummyEntity = new DummyEntity(new Vector(1.5, 1.5, 1.5), 0, Vector.ZERO_VECTOR);
        final Hitbox hitbox = new Hitbox(dummyEntity, new Vector(-0.5, -0.5, -0.5), new Vector(1, 1, 1));
        dummyEntity.setOrient(Math.PI / 4);

        final Entity dummyEntity2 = new DummyEntity(new Vector(2.5, 1.5, 2.5), 0, Vector.ZERO_VECTOR);
        final Hitbox hitbox2 = new Hitbox(dummyEntity, new Vector(-0.5, -0.5, -0.5), new Vector(1, 1, 1));

        final Vector minTrans = hitbox2.minTranslation(hitbox);
        assertEquals(0, minTrans.getX());
        assertEquals(-1, minTrans.getY(), 0.01);
        assertEquals(0, minTrans.getZ());

        final Entity dummyEntity3 = new DummyEntity(new Vector(100, 100, 100), 0, Vector.ZERO_VECTOR);
        final Hitbox hitbox3 = new Hitbox(dummyEntity3, new Vector(-2, -2, -2), new Vector(4, 4, 4));

        final Vector minTrans2 = hitbox.minTranslation(hitbox3);
        assertEquals(0, minTrans2.getX());
        assertEquals(0, minTrans2.getY());
        assertEquals(0, minTrans2.getZ());
    }
}
