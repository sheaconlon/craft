package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.ui.UserInterface;

/**
 * A launcher of Realcraft.
 */
public class Launcher {
    /**
     * Launch Realcraft.
     */
    private void launch() {
        final UserInterface ui = new UserInterface();
        ui.show();
        while (!ui.shouldClose()) {
            ui.respond();
        }
    }

    /**
     * The main entry point of Realcraft.
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        final Launcher launcher = new Launcher();
        launcher.launch();
    }
}
