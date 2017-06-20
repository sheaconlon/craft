package com.sheaconlon.realcraft.ui;

import com.sheaconlon.realcraft.world.World;

import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

/**
 * A user interface for Realcraft.
 */
public class UserInterface {
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
        this.window.setWindowCloseCallback(this.windowCloseCallback);
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
     */
    public void respond(final World world) {
        window.runCallbacks();
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
}
