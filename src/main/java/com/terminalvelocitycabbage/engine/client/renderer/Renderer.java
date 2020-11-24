package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class Renderer {

	// The window handle
	private static Window window;
	protected static Camera camera;
	protected Matrix4f viewMatrix = new Matrix4f();
	private static float[] frameTimes = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,};
	private static long startFrameTime = 0;
	private static long endFrameTime = 0;

	public Renderer(int width, int height, String title, InputHandler inputHandler) {
		window = new Window(width, height, title, true, inputHandler, true, true);
	}

	public void run() {
		start();
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
		window.show();
		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();
		//Transparent stuff
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void start() {
		while (!glfwWindowShouldClose(getWindow().getID())) {
			startFrameTime = System.nanoTime();
			loop();
			endFrameTime = System.nanoTime();
			calcFrameTime();
		}
	}

	private void calcFrameTime() {
		long deltaTime = endFrameTime - startFrameTime;
		float timeMillis = TimeUnit.MILLISECONDS.convert(deltaTime, TimeUnit.NANOSECONDS);
		for (int i = 0; i < frameTimes.length - 1; i++) {
			frameTimes[i] = frameTimes[i + 1];
		}
		frameTimes[frameTimes.length - 1] = timeMillis;
	}

	public float getFrameTimeAverageMillis() {
		float total = 0;
		for (int i = 0; i < frameTimes.length; i++) {
			total += frameTimes[i];
		}
		return total / frameTimes.length;
	}

	public float getFramerate() {
		return 1 / getFrameTimeAverageMillis() * 1000;
	}

	public void destroy() {
		// Free the window callbacks and destroy the window
		window.destroy();

		// Terminate GLFW and free the error callback
		glfwTerminate();
		Objects.requireNonNull(glfwSetErrorCallback(null)).free();
	}

	public static Window getWindow() {
		return window;
	}

	public void loop() {
		if (window.isResized()) {
			window.updateDisplay();
			camera.updateProjectionMatrix(window.width(), window.height());
		}
	}

	public void push() {
		glfwSwapBuffers(window.getID());
		glfwPollEvents();
	}
}
