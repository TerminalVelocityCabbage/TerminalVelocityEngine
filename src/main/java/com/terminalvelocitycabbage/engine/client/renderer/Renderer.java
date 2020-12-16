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
	private static long previousFrameTime;

	public Renderer(int width, int height, String title, InputHandler inputHandler) {
		window = new Window(width, height, title, false, inputHandler, true, true);
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
		//TODO make ways to swap between blend functions like render layers
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void start() {
		while (!glfwWindowShouldClose(getWindow().getID())) {
			previousFrameTime = startFrameTime;
			startFrameTime = System.nanoTime();
			loop();
			endFrameTime = System.nanoTime();
			calcFrameTime();
		}
	}

	private void calcFrameTime() {
		if (frameTimes.length - 1 >= 0) System.arraycopy(frameTimes, 1, frameTimes, 0, frameTimes.length - 1);
		frameTimes[frameTimes.length - 1] = TimeUnit.MILLISECONDS.convert(endFrameTime - startFrameTime, TimeUnit.NANOSECONDS);
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

	//TODO animation smoothness (don't hard code in 20)
	public float getDeltaTime() {
		return (startFrameTime - previousFrameTime) / 1e9f * 20;
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
