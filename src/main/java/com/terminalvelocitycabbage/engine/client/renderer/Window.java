package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputListener;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.templates.events.client.WindowResizeEvent;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private long windowID;

	private int windowWidth;
	private int windowHeight;

	private int framebufferWidth;
	private int framebufferHeight;

	private String title;
	private boolean vSync;
	private boolean center;
	private boolean lockAndHideCursor;

	private boolean isResized;
	private GLFWWindowSizeCallback sizeCallback;

	private int monitorWidth;
	private int monitorHeight;

	public double cursorX;
	public double cursorY;

	Matrix4f orthoProjectionMatrix;

	private InputListener inputListener;

	public Window(int width, int height, String title, boolean vSync, boolean center, boolean lockAndHideCursor) {
		this.windowWidth = width;
		this.windowHeight = height;
		this.title = title;
		this.vSync = vSync;
		this.center = center;
		this.lockAndHideCursor = lockAndHideCursor;
		orthoProjectionMatrix = new Matrix4f();
	}

	public void create() {
		long tID = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
		if (tID == NULL) {
			Log.crash("Failed to Initialize", "an error occurred trying to init the game", new RuntimeException("Failed to create the GLFW window"));
		}
		windowID = tID;
	}

	public void init() {
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {

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
				glfwSetWindowPos(windowID, (monitorWidth - windowWidth) / 2, (monitorHeight - windowHeight) / 2);
			}

			if (lockAndHideCursor) {
				glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
			}

			sizeCallback = GLFWWindowSizeCallback.create(this::windowSizeCallback);
			glfwSetWindowSizeCallback(windowID, sizeCallback);

			glfwSetFramebufferSizeCallback(windowID, (handle, w, h) -> {
				framebufferWidth = w;
				framebufferHeight = h;
			});
		}
		inputListener = new InputListener(this);
	}

	public void queueClose() {
		glfwSetWindowShouldClose(windowID, true);
	}

	public boolean isResized() {
		return isResized;
	}

	public void updateDisplay() {
		glViewport(0, 0, this.windowWidth, this.windowHeight);
		isResized = false;
	}

	private void windowSizeCallback(long window, int w, int h) {
		this.windowWidth = w;
		this.windowHeight = h;
		isResized = true;
		ClientBase.getInstance().dispatchEvent(new WindowResizeEvent(WindowResizeEvent.EVENT));
	}

	public void destroy() {
		glfwFreeCallbacks(windowID);
		glfwDestroyWindow(windowID);
	}

	public void setvSync(boolean vSync) {
		this.vSync = vSync;
		glfwSwapInterval(this.vSync ? 1 : 0);
	}

	public void show() {
		glfwMakeContextCurrent(windowID);
		setvSync(vSync);
		glfwShowWindow(windowID);
	}

	public void showCursor() {
		if (lockAndHideCursor) {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			lockAndHideCursor = false;
		}
	}

	public void hideCursor() {
		if (!lockAndHideCursor) {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
			lockAndHideCursor = true;
		}
	}

	public double getCursorX() {
		return ((cursorX / width()) * 2) - 1;
	}

	public double getCursorY() {
		return ((-cursorY / height()) * 2) + 1;
	}

	public long getID() {
		return windowID;
	}

	public int monitorWidth() {
		return monitorWidth;
	}

	public int monitorHeight() {
		return monitorHeight;
	}

	public int width() {
		return windowWidth;
	}

	public int height() {
		return windowHeight;
	}



	public float aspectRatio() {
		return windowWidth / (float)windowHeight;
	}

	public Matrix4f getOrthoProjectionMatrix() {
		orthoProjectionMatrix.identity();
		orthoProjectionMatrix.setOrtho2D(0, windowWidth, windowHeight, 0);
		return orthoProjectionMatrix;
	}

	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(windowID, title);
	}

	public void focus() {
		glfwFocusWindow(windowID);
	}

	public InputListener getInputListener() {
		return inputListener;
	}
}
