package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private long windowID;

	private int windowWidth;
	private int windowHeight;
	private String title;
	private boolean vSync;
	private RendererBase renderer;
	private InputHandler inputHandler;
	private boolean center;

	private int monitorWidth;
	private int monitorHeight;

	public Window(int width, int height, String title, boolean vSync, RendererBase renderer, InputHandler inputHandler, boolean center) {
		this.windowWidth = width;
		this.windowHeight = height;
		this.title = title;
		this.vSync = vSync;
		this.renderer = renderer;
		this.inputHandler = inputHandler;
		this.center = center;
	}

	public void create() {
		long tID = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
		if (tID == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		windowID = tID;
	}

	public void init() {
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		inputHandler.processInput(windowID);

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(windowID, pWidth, pHeight);

			//Update window instance dimensions
			windowWidth = pWidth.get();
			windowHeight = pHeight.get();

			// Get the resolution of the primary monitor
			GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			if (videoMode == null) {
				Log.error("Could not start window");
				System.exit(-1);
			}

			//Make sure monitorHeight is correct
			monitorWidth = videoMode.width();
			monitorHeight = videoMode.height();

			// Center the window
			if (center) {
				glfwSetWindowPos(windowID, (videoMode.width() - pWidth.get(0))/2, (videoMode.height() - pHeight.get(0))/2);
			}
		}
	}

	public void destroy() {
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
	}

	public void show() {
		glfwMakeContextCurrent(windowID);
		if (vSync) {
			glfwSwapInterval(1);
		}
		glfwShowWindow(windowID);
	}

	public long getWindow() {
		return windowID;
	}

	public int getMonitorWidth() {
		return monitorWidth;
	}

	public int getMonitorHeight() {
		return monitorHeight;
	}

	public int width() {
		return windowWidth;
	}

	public int height() {
		return windowHeight;
	}
}
