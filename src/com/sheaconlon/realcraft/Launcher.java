package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.generator.Generator;
import com.sheaconlon.realcraft.renderer.Prerenderer;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.simulator.Simulator;
import com.sheaconlon.realcraft.ui.UserInterface;
import com.sheaconlon.realcraft.world.World;

public class Launcher {
    public static void main(final String[] args) {
        final World world = new World();
        final UserInterface ui = new UserInterface(world);
        ui.show();
        final Renderer renderer = new Renderer(world, ui.getWindowHandle(), ui.getDimensions());
        final Thread rendererThread = new Thread(renderer);
        final Thread generatorThread = new Thread(new Generator(world));
        final Thread prerendererThread = new Thread(new Prerenderer(world, renderer));
        final Thread simulatorThread = new Thread(new Simulator(world));
        rendererThread.start();
        generatorThread.start();
        prerendererThread.start();
        simulatorThread.start();
        ui.run();
        rendererThread.interrupt();
        generatorThread.interrupt();
        prerendererThread.interrupt();
        simulatorThread.interrupt();
    }
}