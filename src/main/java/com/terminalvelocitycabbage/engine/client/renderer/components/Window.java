package com.terminalvelocitycabbage.engine.client.renderer.components;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

	private long windowID;

	private int windowWidth;
	private int windowHeight;
	private String title;
	private boolean vSync;
	private InputHandler inputHandler;
	private boolean center;
	private boolean lockAndHideCursor;

	private boolean isResized;
	private GLFWWindowSizeCallback sizeCallback;

	private int monitorWidth;
	private int monitorHeight;

	public double cursorX;
	public double cursorY;

	Matrix4f orthoProjectionMatrix;

	public Window(int width, int height, String title, boolean vSync, InputHandler inputHandler, boolean center, boolean lockAndHideCursor) {
		this.windowWidth = width;
		this.windowHeight = height;
		this.title = title;
		this.vSync = vSync;
		this.inputHandler = inputHandler;
		this.center = center;
		this.lockAndHideCursor = lockAndHideCursor;
		orthoProjectionMatrix = new Matrix4f();
	}

	public void create() {
		long tID = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
		if (tID == NULL) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		windowID = tID;
	}

	public void init() {
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

			if (lockAndHideCursor) {
				glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
			}

			sizeCallback = GLFWWindowSizeCallback.create(this::windowSizeCallback);
			glfwSetWindowSizeCallback(windowID, sizeCallback);

			//init the input handler
			inputHandler.init(this);
		}
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
		ClientBase.instance.dispatchEvent(new WindowResizeEvent(WindowResizeEvent.EVENT));
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

	public boolean isLeftMouseJustReleased() {
		return inputHandler.isLeftButtonReleased();
	}

	public boolean isLeftMouseDown() {
		return inputHandler.isLeftButtonPressed();
	}

	public boolean isRightMouseJustReleased() {
		return inputHandler.isRightButtonReleased();
	}

	public boolean isRightMouseDown() {
		return inputHandler.isRightButtonPressed();
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

	public InputHandler getInputHandler() {
		return inputHandler;
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
}
