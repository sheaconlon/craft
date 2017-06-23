package com.sheaconlon.realcraft.renderer;

/**
 * A thing which knows how to render some thing.
 */
abstract class Renderer2<T> {
    /**
     * Create a renderer for some thing.
     * @param thing The thing.
     */
    protected Renderer2(final T thing) {

    }

    /**
     * Render the thing. An OpenGL context must be current.
     */
    abstract void render();
}
