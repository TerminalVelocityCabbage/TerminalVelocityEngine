package com.terminalvelocitycabbage.engine.client.renderer.window;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputListener;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.templates.events.client.WindowResizeEvent;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static java.util.Objects.requireNonNull;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
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
	private boolean fullscreen;
	private boolean blit;

	private boolean isResized;
	private GLFWWindowSizeCallback sizeCallback;

	private long primaryMonitor;
	private int monitorWidth;
	private int monitorHeight;

	public double cursorX;
	public double cursorY;

	Matrix4f orthoProjectionMatrix;

	private InputListener inputListener;
	private float contentScaleX;
	private float contentScaleY;

	public Window(WindowConfig windowConfig) {
		this.windowWidth = windowConfig.width;
		this.windowHeight = windowConfig.height;
		this.title = windowConfig.title;
		this.vSync = windowConfig.vSync;
		this.center = windowConfig.center;
		this.lockAndHideCursor = windowConfig.lockAndHideCursor;
		orthoProjectionMatrix = new Matrix4f();
		this.fullscreen = windowConfig.fullscreen;
		this.blit = windowConfig.blit;
	}

	public void create(boolean debugMode) {

		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) {
			Log.crash("Initialization Error", new IllegalStateException("Unable to initialize GLFW"));
		}

		// Configure GLFW
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

		//If rendering offscreen to an fbo and blitting to screen
		if (blit) {
			glfwWindowHint(GLFW_DEPTH_BITS, 0);
			glfwWindowHint(GLFW_STENCIL_BITS, 0);
			glfwWindowHint(GLFW_ALPHA_BITS, 0);
		}

		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_DOUBLEBUFFER, GLFW_TRUE);

		//glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);
		if (fullscreen) {
			glfwWindowHint(GLFW_FLOATING, GLFW_TRUE);
			glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
		}
		if (debugMode) {
			glfwWindowHint( GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE );
		}

		primaryMonitor = glfwGetPrimaryMonitor();
		windowWidth = fullscreen ? 1 : windowWidth;
		windowHeight = fullscreen ? 1 : windowHeight;

		windowID = glfwCreateWindow(windowWidth, windowHeight, title, fullscreen ? primaryMonitor : NULL, NULL);

		if (lockAndHideCursor) {
			glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		}

		if (windowID == NULL) {
			Log.crash("Failed to Initialize", "an error occurred trying to init the game", new RuntimeException("Failed to create the GLFW window"));
		}
	}

	public void init() {
		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {

			// Get the resolution of the primary monitor
			GLFWVidMode videoMode = glfwGetVideoMode(primaryMonitor);

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

			sizeCallback = GLFWWindowSizeCallback.create(this::windowSizeCallback);
			glfwSetWindowSizeCallback(windowID, sizeCallback);

			//Setup framebuffer size stuff
			glfwSetFramebufferSizeCallback(windowID, (handle, w, h) -> {
				framebufferWidth = w;
				framebufferHeight = h;
			});
			IntBuffer fw = stack.mallocInt(1);
			IntBuffer   fh = stack.mallocInt(1);
			glfwGetFramebufferSize(windowID, fw, fh);
			framebufferWidth = fw.get(0);
			framebufferHeight = fh.get(0);

			//Setup content scale stuff
			glfwSetWindowContentScaleCallback(windowID, (handle, xscale, yscale) -> {
				contentScaleX = xscale;
				contentScaleY = yscale;
			});
			FloatBuffer sx = stack.mallocFloat(1);
			FloatBuffer sy = stack.mallocFloat(1);
			glfwGetWindowContentScale(windowID, sx, sy);
			contentScaleX = sx.get(0);
			contentScaleY = sy.get(0);
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
		// Terminate GLFW and free the error callback
		glfwTerminate();
		requireNonNull(glfwSetErrorCallback(null)).free();
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

	public double getCursorXOGL() {
		return ((cursorX / width()) * 2) - 1;
	}

	public double getCursorYOGL() {
		return ((-cursorY / height()) * 2) + 1;
	}

	public double getCursorX() {
		return cursorX;
	}

	public double getCursorY() {
		return cursorY;
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

	public int getFramebufferWidth() {
		return framebufferWidth;
	}

	public int getFramebufferHeight() {
		return framebufferHeight;
	}

	public float getContentScaleX() {
		return contentScaleX;
	}

	public float getContentScaleY() {
		return contentScaleY;
	}

	public float getEffectiveWidth() {
		return framebufferWidth / contentScaleX;
	}

	public float getEffectiveHeight() {
		return framebufferHeight / contentScaleY;
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

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public String getTitle() {
		return title;
	}

	public boolean isvSync() {
		return vSync;
	}

	public boolean isCenter() {
		return center;
	}

	public boolean isLockAndHideCursor() {
		return lockAndHideCursor;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public boolean isBlit() {
		return blit;
	}

	public int getMonitorWidth() {
		return monitorWidth;
	}

	public int getMonitorHeight() {
		return monitorHeight;
	}
}
