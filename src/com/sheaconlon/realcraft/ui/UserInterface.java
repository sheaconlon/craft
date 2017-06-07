package com.sheaconlon.realcraft.ui;

import org.lwjgl.glfw.GLFWWindowCloseCallbackI;

/**
 * A user interface for Realcraft.
 */
public class UserInterface {
    private class WindowCloseCallback implements GLFWWindowCloseCallbackI {
        public void invoke(final long windowHandle) {
            UserInterface.this.shouldClose = true;
        }
    }

    /**
     * The window this user interface is displayed in.
     */
    private final Window window;

    /**
     * This user interface's callback for window close events.
     *
     * This instance attribute exists to maintain a strong reference to the callback so that it is not garbage
     * collected.
     */
    private final WindowCloseCallback windowCloseCallback;


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
        this.window = new Window();
        // Save a strong reference to the callback so that it is not garbage collected.
        this.windowCloseCallback = new UserInterface.WindowCloseCallback();
        this.window.setWindowCloseCallback(this.windowCloseCallback);
        this.shouldClose = false;
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
     * Respond to input.
     */
    public void respond() {
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
