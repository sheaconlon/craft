package com.sheaconlon.realcraft.ui;

import com.sheaconlon.realcraft.world.World;
import com.sheaconlon.realcraft.positioning.Position;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallbackI;
import org.lwjgl.glfw.GLFWKeyCallbackI;

/**
 * A user interface for Realcraft.
 */
public class UserInterface {
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
     * Whether the user interface should close.
     */
    private boolean shouldClose;

    /**
     * Construct a user interface.
     *
     * The user interface will not be visible, but can be made visible by a call to {@link #show()}.
     */
    public UserInterface() {
        System.out.println("setting up UI...");
        this.window = new Window();
        // Save a strong reference to the callback so that it is not garbage collected.
        this.windowCloseCallback = new UserInterface.WindowCloseCallback();
        this.keyCallback = new UserInterface.KeyCallback();
        this.window.setWindowCloseCallback(this.windowCloseCallback);
        this.window.setKeyCallback(this.keyCallback);
        this.shouldClose = false;
        System.out.println("done with UI");
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
     * Respond to input.
     * @param world The world.
     * @param elapsedTime The estimated amount of time that has elapsed since the last call to this method, in
     *                    seconds.
     */
    public void respond(final World world, final double elapsedTime) {
        window.runCallbacks();
        this.respondToMovement(world, elapsedTime);
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
            directionX += relativeDirectionZ * Math.sin(orientation);
            double directionZ = relativeDirectionZ * Math.cos(orientation);
            directionZ += relativeDirectionX * Math.sin(orientation);
            final double magnitude = Math.sqrt(directionX*directionX + directionZ*directionZ);
            directionX /= magnitude;
            directionZ /= magnitude;
            final double distance = UserInterface.SPEED_OF_MOVEMENT * elapsedTime;
            final Position anchor = world.getPlayer().getAnchor();
            anchor.changeX(directionX * distance);
            anchor.changeZ(directionZ * distance);
        }
    }
}
