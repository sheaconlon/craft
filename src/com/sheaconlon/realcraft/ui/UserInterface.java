package com.sheaconlon.realcraft.ui;

import com.sheaconlon.realcraft.concurrency.Worker;
import com.sheaconlon.realcraft.renderer.Renderer;
import com.sheaconlon.realcraft.utilities.Vector;
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
    // TODO: Detect screen FPS and use that as the target tick rate of UserInterface.
    /**
     * A user interface's return value for {@link #getTargetFreq()}.
     */
    private static final long TARGET_FREQ = 60;

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
     * Construct a user interface.
     *
     * The user interface will not be visible, but can be made visible by a call to {@link #show()}.
     */
    public UserInterface(final World world) {
        this.world = world;
        this.window = new Window();
        // Save a strong reference to the callback so that it is not garbage collected.
        this.windowCloseCallback = new UserInterface.WindowCloseCallback();
        this.keyCallback = new UserInterface.KeyCallback();
        this.window.setWindowCloseCallback(this.windowCloseCallback);
        this.window.setKeyCallback(this.keyCallback);
        this.shouldClose = false;
        this.cursorPosition = this.window.getCursorPosition();
        this.window.show();
    }

    @Override
    protected double getTargetFreq() {
        return UserInterface.TARGET_FREQ;
    }

    public void initInThread() {

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
    public void tick(final double elapsedTime) {
        window.runCallbacks();
        this.respondToMovement(elapsedTime);
        this.respondToLooking(elapsedTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldStop() {
        return this.shouldClose;
    }

    /**
     * Close the user interface.
     */
    public void close() {
        this.window.close();
    }

    /**
     * Respond to input that requests movement.
     * @param elapsedTime The estimated amount of time that has elapsed since the last call to this method, in
     *                    seconds.
     */
    private void respondToMovement(final double elapsedTime) {
        Vector displacement = new Vector(0, 0, 0);
        if (this.window.wKeyIsPressed()) {
            // towards positive x
            displacement = Vector.add(displacement, new Vector(1, 0, 0));
        }
        if (this.window.aKeyIsPressed()) {
            // towards negative z
            displacement = Vector.add(displacement, new Vector(0, 0, -1));
        }
        if (this.window.sKeyIsPressed()) {
            // towards negative x
            displacement = Vector.add(displacement, new Vector(-1, 0, 0));
        }
        if (this.window.dKeyIsPressed()) {
            // towards positive z
            displacement = Vector.add(displacement, new Vector(0, 0, 1));
        }
        if (!displacement.equals(Vector.ZERO_VECTOR)) {
            displacement = Vector.rotateHorizontal(displacement, this.world.getPlayer().getHorizontalOrientation());
            final double distance = UserInterface.SPEED_OF_MOVEMENT * elapsedTime;
            displacement = Vector.scale(displacement, distance / displacement.mag());
            this.world.getPlayer().changePosition(displacement);
        }
    }

    /**
     * Respond to input that requests looking.
     * @param elapsedTime The estimated amount of time that has elapsed since the last call to this method, in
     *                    seconds.
     */
    private void respondToLooking(final double elapsedTime) {
        final double[] cursorPositionDelta = this.getCursorPositionDelta();
        double horizontalOrientationDelta = cursorPositionDelta[0] * UserInterface.LOOKING_FACTOR;
        double verticalOrientationDelta = -cursorPositionDelta[1] * UserInterface.LOOKING_FACTOR;
        final double limit = UserInterface.LOOKING_MAX_SPEED * elapsedTime;
        horizontalOrientationDelta = Math.signum(horizontalOrientationDelta) * Math.min(limit, Math.abs(horizontalOrientationDelta));
        verticalOrientationDelta = Math.signum(verticalOrientationDelta) * Math.min(limit, Math.abs(verticalOrientationDelta));
        this.world.getPlayer().changeHorizontalOrientation(horizontalOrientationDelta);
        this.world.getPlayer().changeVerticalOrientation(verticalOrientationDelta);
    }
}
