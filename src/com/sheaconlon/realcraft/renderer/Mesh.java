package com.sheaconlon.realcraft.renderer;

import com.sheaconlon.realcraft.utilities.Vector;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The graphical representation of an object.
 */
public class Mesh {
    private final List<Quad> quads;

    /**
     * Create a mesh.
     * @param quads The quads of the mesh.
     */
    public Mesh(final Collection<? extends Quad> quads) {
        this.quads = new LinkedList<>(quads);
    }

    /**
     * Get the quads of this mesh.
     * @return The quads of this mesh.
     */
    public List<Quad> quads() {
        return Collections.unmodifiableList(this.quads);
    }

    /**
     * Translate this mesh.
     * @param disp The displacement to apply.
     * @return A new mesh which is like this mesh, with all positions translated by {@code disp}.
     */
    public Mesh translate(final Vector disp) {
        final List<Quad> newQuads = this.quads.stream()
                .map(quad -> quad.translate(disp))
                .collect(Collectors.toList());
        return new Mesh(newQuads);
    }
}
