package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.generator.Generator;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.ui.UserInterface;
import com.sheaconlon.realcraft.world.World;

public class Launcher {
    public static void main(final String[] args) {
        final World world = new World();
        final UserInterface ui = new UserInterface(world);
        ui.show();
        final Thread rendererThread = new Thread(new Renderer(world, ui.getWindowHandle(), ui.getDimensions()));
        final Thread generatorThread = new Thread(new Generator(world));
        generatorThread.start();
        rendererThread.start();
        ui.run();
        rendererThread.interrupt();
        generatorThread.interrupt();
    }
}