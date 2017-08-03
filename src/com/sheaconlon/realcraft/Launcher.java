package com.sheaconlon.realcraft;

import com.sheaconlon.realcraft.concurrency.Runner;
import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.generator.Generator;
import com.sheaconlon.realcraft.renderer.Prerenderer;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.ui.UserInterface;
import com.sheaconlon.realcraft.world.World;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Launcher {
    public static void main(final String[] args) {
        final World world = new World();

        final UserInterface ui = new UserInterface(world);
        final Renderer renderer = new Renderer(world, ui);
        final Generator generator = new Generator(world);
        final Prerenderer prerenderer = new Prerenderer(world, renderer);
        final Worker[] workers = new Worker[]{
                ui,
                renderer,
                generator,
                prerenderer
        };

        final Queue<Worker> sharedWorkers = new PriorityBlockingQueue<>();
        final int nThreads = Runtime.getRuntime().availableProcessors(); // TODO: Check value periodically per https://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html#availableProcessors().
        final Runner[] runners = new Runner[nThreads];
        for (int i = 0; i < runners.length; i++) {
            runners[i] = new Runner(sharedWorkers);
        }
        int dedicatedCurrRunner = 1 % runners.length;
        for (final Worker w : workers) {
            if (w.needsDedicatedThread()) {
                if (w.needsMainThread()) {
                    runners[0].assign(w);
                } else {
                    runners[dedicatedCurrRunner].assign(w);
                    dedicatedCurrRunner = (dedicatedCurrRunner + 1) % runners.length;
                }
            } else {
                sharedWorkers.add(w);
            }
        }

        Thread.currentThread().setName("Runner #0");
        final Thread[] threads = new Thread[runners.length - 1];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(runners[i + 1]);
            threads[i].setName("Runner #" + (i + 1));
            threads[i].start();
        }
        runners[0].run();
        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
        }
    }
}