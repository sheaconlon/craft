package com.sheaconlon.realcraft.ui;

import com.sheaconlon.realcraft.Worker;
import com.sheaconlon.realcraft.world.World;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.BufferUtils;
import org.lwjgl.system.CallbackI;

import java.nio.DoubleBuffer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * A user interface for Realcraft.
 */
public class UserInterface extends Worker {
    /**
     * The number of nanoseconds in a second.
     */
    private static final double NANOSECONDS_PER_SECOND = Math.pow(10, 9);

    /**
     * The number of radians that the player's orientation should change by for each pixel of mouse movement.
     */
    private static final double LOOKING_FACTOR = 0.003;

    /**
     * The maximum angular speed, in radians per second, at which the player may change their orientation.
     */
    private static final double LOOKING_MAX_SPEED = 1.5 * Math.PI;

    /**
     * The desired speed of the user's movement, in blocks per second.
     */
    private static final double SPEED_OF_MOVEMENT = 2;

    /**
     * The user interface's callback for window close events.
     */
    private class WindowCloseCallback implements GLFWWindowCloseCallbackI {
        /**
         * Respond to a window close event.
         * @param windowHandle The handle of the GLFW window in which the event occured.
         */
        public void invoke(final long windowHandle) {
            UserInterface.this.shouldClose = true;
        }
    }

    /**
     * The user interface's callback for key events.
     */
    private class KeyCallback implements GLFWKeyCallbackI {
        /**
         * {@inheritDoc}
         */
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key == GLFW.GLFW_KEY_ESCAPE) {
                UserInterface.this.shouldClose = true;
            }
        }
    }

    /**
     * The window this user interface is displayed in.
     */
    private final Window window;

    /**
     * The user interface's callback for window close events.
     *
     * This instance attribute exists to maintain a strong reference to the callback so that it is not garbage
     * collected.
     */
    private final UserInterface.WindowCloseCallback windowCloseCallback;

    /**
     * The user interface's callback for key events.
     *
     * This instance attribute exists to maintain a strong reference to the callback so that it is not garbage
     * collected.
     */
    private final UserInterface.KeyCallback keyCallback;

    /**
     * The world.
     */
    private World world;

    /**
     * Whether the user interface should close.
     */
    private boolean shouldClose;

    /**
     * The most recently recorded cursor position, as an array with x- and y-coordinates.
     */
    private double[] cursorPosition;

    /**
     * The 20 most recent elapsed times in calls to {@link #respond(World, double)}.
     */
    private Deque<Double> elapsedTimes;

    /**
     * The sum of the elements of {@link #elapsedTimes}.
     */
    private double elapsedTimesSum;

    /**
     * The time of the last call to {@link #tick()}, in nanoseconds since some arbitrary point.
     */
    private long lastTickTime;

    /**
     * Construct a user interface.
     *
     * The user interface will not be visible, but can be made visible by a call to {@link #show()}.
     */
    public UserInterface(final World world) {
        this.world = world;
        this.lastTickTime = System.nanoTime();
        this.window = new Window();
        // Save a strong reference to the callback so that it is not garbage collected.
        this.windowCloseCallback = new UserInterface.WindowCloseCallback();
        this.keyCallback = new UserInterface.KeyCallback();
        this.window.setWindowCloseCallback(this.windowCloseCallback);
        this.window.setKeyCallback(this.keyCallback);
        this.shouldClose = false;
        this.cursorPosition = this.window.getCursorPosition();
        this.elapsedTimes = new LinkedList<Double>();
        this.elapsedTimesSum = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void inThreadInitialize() {

    }

    /**
     * Show the user interface.
     */
    public void show() {
        this.window.show();
    }

    /**
     * Return whether the user interface might be visible.
     * @return Whether the user interface might be visible.
     */
    public boolean mightBeVisible() {
        return !this.window.isIconified();
    }

    /**
     * Return whether the user interface is focused.
     * @return Whether the user interface is focused.
     */
    public boolean isFocused() {
        return this.window.isFocused();
    }

    /**
     * Get the dimensions of this user interface.
     * @return The dimensions of this user interface in screen coordinates, as a array containing the width and
     * height.
     */
    public int[] getDimensions() {
        return this.window.getDimensions();
    }

    /**
     * Get the handle of the window for this user interface.
     * @return The handle of the window for this user interface.
     */
    public long getWindowHandle() {
        return this.window.getHandle();
    }

    /**
     * Get the change in the cursor's position since the last call to this method.
     * @return The change in the cursor's position since the last call to this method, as an array of changes
     * in x- and y-coordinate.
     */
    private double[] getCursorPositionDelta() {
        final double[] currentCursorPosition = this.window.getCursorPosition();
        final double[] delta = new double[]{
                currentCursorPosition[0] - this.cursorPosition[0],
                currentCursorPosition[1] - this.cursorPosition[1]
        };
        this.cursorPosition = currentCursorPosition;
        return delta;
    }

    /**
     * Respond to input.
     */
    public void tick() {
        double elapsedTime = (System.nanoTime() - this.lastTickTime) / UserInterface.NANOSECONDS_PER_SECOND;
        if (elapsedTime < 1.0/120) {
            try {
                Thread.sleep((long) ((1.0 / 120 - elapsedTime) * Math.pow(10, 3)));
            } catch (final InterruptedException e) {};
            elapsedTime = (System.nanoTime() - this.lastTickTime) / UserInterface.NANOSECONDS_PER_SECOND;
        }
        lastTickTime = System.nanoTime();
        if (this.elapsedTimes.size() == 20) {
            this.elapsedTimesSum -= this.elapsedTimes.removeFirst();
            this.elapsedTimes.addLast(elapsedTime);
            this.elapsedTimesSum += elapsedTime;
            System.out.println(20 / this.elapsedTimesSum);
        } else {
            this.elapsedTimes.addLast(elapsedTime);
            this.elapsedTimesSum += elapsedTime;
        }
        window.runCallbacks();
        this.respondToMovement(world, elapsedTime);
        this.respondToLooking(world, elapsedTime);
    }

    /**
     * Return whether the user interface should close.
     * @return Whether the user interface should close.
     */
    public boolean shouldClose() {
        return this.shouldClose;
    }

    /**
     * Close the user interface.
     */
    public void close() {
        this.window.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        this.inThreadInitialize();
        while (!this.shouldClose()) {
            this.tick();
        }
    }

    /**
     * Respond to input that requests movement.
     * @param world The world.
     * @param elapsedTime The estimated amount of time that has elapsed since the last call to this method, in
     *                    seconds.
     */
    private void respondToMovement(final World world, final double elapsedTime) {
        double relativeDirectionX = 0;
        double relativeDirectionZ = 0;
        if (this.window.wKeyIsPressed()) {
            // towards negative z
            relativeDirectionX += 1;
        }
        if (this.window.aKeyIsPressed()) {
            // towards negative x
            relativeDirectionZ += -1;
        }
        if (this.window.sKeyIsPressed()) {
            // towards positive z
            relativeDirectionX += -1;
        }
        if (this.window.dKeyIsPressed()) {
            // towards positive x
            relativeDirectionZ += 1;
        }
        if (relativeDirectionX != 0 || relativeDirectionZ != 0) {
            final double orientation = world.getPlayer().getOrientation();
            double directionX = relativeDirectionX * Math.cos(orientation);
            directionX += relativeDirectionZ * -Math.sin(orientation);
            double directionZ = relativeDirectionZ * Math.cos(orientation);
            directionZ += relativeDirectionX * Math.sin(orientation);
            final double magnitude = Math.sqrt(directionX*directionX + directionZ*directionZ);
            directionX /= magnitude;
            directionZ /= magnitude;
            final double distance = UserInterface.SPEED_OF_MOVEMENT * elapsedTime;
            final double[] deltaPosition = new double[]{
                    directionX * distance,
                    0,
                    directionZ * distance
            };
            world.getPlayer().changePosition(deltaPosition);
        }
    }

    /**
     * Respond to input that requests looking.
     * @param world The world.
     * @param elapsedTime The estimated amount of time that has elapsed since the last call to this method, in
     *                    seconds.
     */
    private void respondToLooking(final World world, final double elapsedTime) {
        final double[] cursorPositionDelta = this.getCursorPositionDelta();
        double orientationDelta = cursorPositionDelta[0] * UserInterface.LOOKING_FACTOR;
        double lookDirectionDelta = -cursorPositionDelta[1] * UserInterface.LOOKING_FACTOR;
        final double limit = UserInterface.LOOKING_MAX_SPEED * elapsedTime;
        orientationDelta = Math.signum(orientationDelta) * Math.min(limit, Math.abs(orientationDelta));
        lookDirectionDelta = Math.signum(lookDirectionDelta) * Math.min(limit, Math.abs(lookDirectionDelta));
        world.getPlayer().changeOrientation(orientationDelta);
        world.getPlayer().changeLookDirection(lookDirectionDelta);
    }
}
