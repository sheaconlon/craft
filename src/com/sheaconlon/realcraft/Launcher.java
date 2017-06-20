package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.ui.UserInterface;
import com.sheaconlon.realcraft.world.ChunkGenerator;
import com.sheaconlon.realcraft.world.World;

/**
 * A launcher of Realcraft.
 */
public class Launcher {
    /**
     * The number of nanoseconds in a second.
     */
    private static final double NANOSECONDS_PER_SECOND = Math.pow(10, 9);

    /**
     * The world.
     */
    private World world;

    /**
     * The user interface.
     */
    private UserInterface ui;

    /**
     * The renderer.
     */
    private Renderer renderer;

    /**
     * The time, in nanoseconds from some start time, of the last tick.
     */
    private long lastTickTime;

    /**
     * Launch Realcraft.
     */
    private void launch() {
        this.world = new World(new ChunkGenerator());
        this.ui = new UserInterface();
        this.ui.show();
        this.renderer = new Renderer(this.ui.getWindowHandle(), this.ui.getDimensions());
        // Pretend that a tick occured upon construction.
        this.lastTickTime = System.nanoTime();
    }

    /**
     * Run Realcraft.
     *
     * Runs until close is requested. Should be called after {@link #launch()}.
     */
    private void run() {
        while (!this.ui.shouldClose()) {
            final long currentTime = System.nanoTime();
            final double elapsedTime = (currentTime - this.lastTickTime) / Launcher.NANOSECONDS_PER_SECOND;
            this.lastTickTime = currentTime;
            this.tick(elapsedTime);
        }
    }

    /**
     * Allow each worker to do some work.
     * @param elapsedTime The estimated amount of time since the last call to this method, in seconds.
     */
    private void tick(final double elapsedTime) {
        this.ui.respond(this.world, elapsedTime);
        this.renderer.render(this.world);
    }

    /**
     * Close Realcraft.
     *
     * Cleans up. Should be called after {@link #run()}.
     */
    private void close() {
        this.ui.close();
    }

    /**
     * The main entry point of Realcraft.
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        final Launcher launcher = new Launcher();
        launcher.launch();
        launcher.run();
        launcher.close();
    }
}
