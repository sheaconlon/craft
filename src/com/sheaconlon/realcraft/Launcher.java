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
     * Launch Realcraft.
     */
    private void launch() {
        this.world = new World(new ChunkGenerator());
        this.ui = new UserInterface();
        this.ui.show();
        this.renderer = new Renderer(this.ui.getWindowHandle(), this.ui.getDimensions());
    }

    // TODO: Use `this` where possible.
    /**
     * Run Realcraft.
     *
     * Runs until close is requested. Should be called after {@link #launch()}.
     */
    private void run() {
        while (!ui.shouldClose()) {
            ui.respond();
            this.renderer.render(this.world);
        }
    }

    /**
     * Close Realcraft.
     *
     * Cleans up. Should be called after {@link #run()}.
     */
    private void close() {
        ui.close();
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
