package com.terminalvelocitycabbage.client.renderer;

import com.terminalvelocitycabbage.client.input.InputHandler;
import com.terminalvelocitycabbage.client.renderer.components.Window;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

public abstract class Renderer {

	// The window handle
	private static Window window;

	public Renderer(int width, int height, String title, InputHandler inputHandler) {
		window = new Window(width, height, title, true, inputHandler, true);
	}

	public void run() {
		window.show();
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();
		loop();
		destroy();
	}

	public void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}

		// Configure GLFW
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		window.create();
		window.init();
	}

	private void destroy() {
		// Free the window callbacks and destroy the window
		window.destroy();

		// Terminate GLFW and free the error callback
		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free();
	}

	public static Window getWindow() {
		return window;
	}

	public abstract void loop();

	public void push() {
		glfwSwapBuffers(window.getID());
		glfwPollEvents();
	}
}
