package com.sheaconlon.realcraft.ui;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

/**
 * A wrapper for a GLFW window object.
 */
public class Window {
    /**
     * The window's callback for GLFW error events.
     */
    private class ErrorCallback implements GLFWErrorCallbackI {
        /**
         * Respond to a GLFW error event.
         * @param errorCode The error code.
         * @param descriptionHandle A handle for a human-readable string describing the error.
         */
        public void invoke(final int errorCode, final long descriptionHandle) {
            throw new RuntimeException("GLFW error, code " + Integer.toString(errorCode) + " occurred");
        }
    }

    /**
     * The width of windows.
     */
    private static final int WIDTH = 100;
    /**
     * The height of windows.
     */
    private static final int HEIGHT = 100;
    /**
     * The title of windows.
     */
    private static final String TITLE = "Realcraft";

    /**
     * The handle of the GLFW window object corresponding to this window.
     */
    private final long handle;


    /**
     * The window's callback for GLFW error events.
     *
     * This instance attribute exists to maintain a strong reference to the callback so that it is not garbage
     * collected.
     */
    private final Window.ErrorCallback errorCallback;

    /**
     * Construct a window.
     *
     * The window will not be visible, but can be made visible by a call to {@link #show()}.
     */
    public Window() {
        final boolean initSuccess = GLFW.glfwInit();
        if (!initSuccess) {
            throw new RuntimeException("GLFW#glfwInit() returned false");
        }
        this.errorCallback = new ErrorCallback();
        GLFW.glfwSetErrorCallback(this.errorCallback);
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, GLFW.GLFW_TRUE);
        this.handle = GLFW.glfwCreateWindow(Window.WIDTH, Window.HEIGHT, Window.TITLE, MemoryUtil.NULL, MemoryUtil.NULL);
        if (this.handle == MemoryUtil.NULL) {
            throw new RuntimeException("GLFW#glfwCreateWindow(...) returned MemoryUtil#NULL");
        }
        // TODO: Restrict aspect ratio so that the player cannot cheat by making their window very wide.
        // TODO: Set window icon.
        // Swap buffers immediately upon calls to GLFW#glfwSwapBuffers. Do not use v-sync.
        GLFW.glfwSwapInterval(0);
    }

    /**
     * Set the callback for window close request events.
     * @param callback The callback.
     */
    void setWindowCloseCallback(final GLFWWindowCloseCallbackI callback) {
        GLFW.glfwSetWindowCloseCallback(this.handle, callback);
    }

    /**
     * Set the callback for framebuffer size change events.
     * @param callback The callback.
     */
    void setFramebufferSizeCallback(final GLFWFramebufferSizeCallbackI callback) {
        GLFW.glfwSetFramebufferSizeCallback(this.handle, callback);
    }

    /**
     * Return whether the window is iconified (minimized).
     * @return Whether the window is iconified (minimized).
     */
    public boolean isIconified() {
        return GLFW.glfwGetWindowAttrib(this.handle, GLFW.GLFW_ICONIFIED) == GLFW.GLFW_TRUE;
    }

    /**
     * Return whether the window is focused.
     * @return Whether the window is focused.
     */
    public boolean isFocused() {
        return GLFW.glfwGetWindowAttrib(this.handle, GLFW.GLFW_FOCUSED) == GLFW.GLFW_TRUE;
    }

    /**
     * Show the window.
     */
    void show() {
        GLFW.glfwShowWindow(this.handle);
    }

    /**
     * Run callbacks for events.
     */
    void runCallbacks() {
        GLFW.glfwPollEvents();
    }

    /**
     * Close the window.
     */
    void close() {
        // Sets all callbacks for this window to MemoryUtil#NULL and frees the old callbacks.
        Callbacks.glfwFreeCallbacks(this.handle);
        // Close the GLFW window.
        GLFW.glfwDestroyWindow(this.handle);
        // Close GLFW.
        GLFW.glfwTerminate();
        // Set the GLFW-wide error callback to null and free the old callback.
        GLFW.glfwSetErrorCallback(null).free();
    }
}
