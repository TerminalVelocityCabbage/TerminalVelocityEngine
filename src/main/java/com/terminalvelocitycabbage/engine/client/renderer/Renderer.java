package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.SceneHandler;
import com.terminalvelocitycabbage.engine.client.renderer.ui.CanvasHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.debug.SystemInfo;
import com.terminalvelocitycabbage.engine.utils.TickManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class Renderer {

	// The window handle
	private static Window window;
	private static float[] frameTimes = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static long endFrameTime = 0;
	private static long previousFrameTime = 0;
	private static long deltaTime = 0;

	private final TickManager tickManager;
	public SceneHandler sceneHandler = new SceneHandler();
	public CanvasHandler canvasHandler = new CanvasHandler();

	public Renderer(int width, int height, String title, InputHandler inputHandler, float tickRate) {
		window = new Window(width, height, title, false, inputHandler, true, true);
		tickManager = new TickManager(tickRate);
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
			Log.crash("Initialization Error", new IllegalStateException("Unable to initialize GLFW"));
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

		//Tell the system information tracker what gpu we are working with here
		SystemInfo.gpuVendor = glGetString(GL_VENDOR);
		SystemInfo.gpuModel = glGetString(GL_RENDERER);
		SystemInfo.gpuVersion = glGetString(GL_VERSION);

		//Transparent stuff
		glEnable(GL_BLEND);
		//TODO make ways to swap between blend functions like render layers
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	private void start() {
		endFrameTime = System.nanoTime();
		while (!glfwWindowShouldClose(getWindow().getID())) {
			previousFrameTime = endFrameTime;
			loop();
			endFrameTime = System.nanoTime();
			calcFrameTime();
		}
	}

	private void calcFrameTime() {
		deltaTime = endFrameTime - previousFrameTime;
		if (frameTimes.length - 1 >= 0) System.arraycopy(frameTimes, 1, frameTimes, 0, frameTimes.length - 1);
		frameTimes[frameTimes.length - 1] = TimeUnit.MILLISECONDS.convert(deltaTime, TimeUnit.NANOSECONDS);
	}

	public float getFrameTimeAverageMillis() {
		float total = 0;
		for (int i = 0; i < frameTimes.length; i++) {
			total += frameTimes[i];
		}
		return total / frameTimes.length;
	}

	public float getDeltaTimeInSeconds() {
		return deltaTime / 1e9f;
	}

	public float getDeltaTimeInMillis() {
		return deltaTime / 1e6f;
	}

	public float getDeltaTime() {
		return deltaTime;
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

		//Tell the tick manager the frame time change
		tickManager.apply(deltaTime / 1e6f);

		//tick as many time as needed
		while (tickManager.hasTick()) {
			sceneHandler.update(deltaTime / 1e6f);
			canvasHandler.tick(getWindow().getCursorX(), getWindow().getCursorY(), getWindow().isLeftMouseJustReleased(), getWindow().isRightMouseJustReleased(), getWindow().getInputHandler().getTicksSinceLastClick());
			//Update input handler last state
			window.getInputHandler().updateMouseButtons();
		}

		if (window.isResized()) {
			window.updateDisplay();
			sceneHandler.getActiveScene().getCamera().updateProjectionMatrix(window.aspectRatio());
		}
	}

	public void push() {
		glfwSwapBuffers(window.getID());
		glfwPollEvents();
	}
}
