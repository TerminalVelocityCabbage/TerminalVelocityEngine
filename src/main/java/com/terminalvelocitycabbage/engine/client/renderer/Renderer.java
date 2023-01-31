package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.shader.ShaderHandler;
import com.terminalvelocitycabbage.engine.client.renderer.util.Capabilities;
import com.terminalvelocitycabbage.engine.debug.SystemInfo;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.profiling.GPUTimer;
import com.terminalvelocitycabbage.engine.utils.TickManager;
import com.terminalvelocitycabbage.templates.ecs.components.CameraComponent;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.system.Callback;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Renderer {

	private static final float[] frameTimes = new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private static long endFrameTime = 0;
	private static long previousFrameTime = 0;
	private static long deltaTime = 0;
	private static long totalTime = 0;

	private final TickManager tickManager;

	private final ShaderHandler shaderHandler = new ShaderHandler();

	private boolean debugMode = false;
	private static Callback debugCallback;

	private static int drawBufferMode;
	private int lastDrawBufferMode;
	private static int polygonMode;
	private int lastPolygonMode;

	long nanoVG;
	GPUTimer gpuTimer;
	Capabilities capabilities;

	public Renderer(float tickRate) {
		tickManager = new TickManager(tickRate);
	}

	public void run() {
		start();
		destroy();
	}

	public void init() {

		// creates the GLCapabilities instance and makes the OpenGL bindings available for use.
		GL.createCapabilities();
		capabilities = new Capabilities();
		capabilities.determineOpenGLCapabilities();

		if (debugMode) {
			glEnable(KHRDebug.GL_DEBUG_OUTPUT_SYNCHRONOUS);
			// Store this callback somewhere so the GC doesn't free it
			debugCallback = GLUtil.setupDebugMessageCallback();
		}

		//Tell the system information tracker what gpu we are working with here
		SystemInfo.gpuVendor = glGetString(GL_VENDOR);
		SystemInfo.gpuModel = glGetString(GL_RENDERER);
		SystemInfo.gpuVersion = glGetString(GL_VERSION);

		//Transparent stuff
		glEnable(GL_BLEND);
		//TODO make ways to swap between blend functions like render modes
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		setBeginMode(PolygonMode.FILL);
		setDrawBufferMode(DrawBufferMode.FRONT_AND_BACK);

		//Init nanovg
		nanoVG = NanoVGGL3.nvgCreate(NanoVGGL3.NVG_ANTIALIAS);
		if (nanoVG == NULL) {
			throw new RuntimeException("Could not init nanovg.");
		}

		//set up the gpu timer for profiling
		gpuTimer = new GPUTimer();
	}

	private void start() {
		endFrameTime = System.nanoTime();
		while (!glfwWindowShouldClose(ClientBase.getWindow().getID())) {
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
		totalTime += deltaTime;
	}

	public float getFrameTimeAverageMillis() {
		float total = 0;
		for (float frameTime : frameTimes) {
			total += frameTime;
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

	public float getTotalTimeInSeconds() {
		return totalTime / 1e9f;
	}

	public void destroy() {

		//Cleanup handlers
		shaderHandler.cleanup();

		//Free debug
		if (debugMode) {
			debugCallback.free();
		}

		//cleanup nanovg
		NanoVGGL3.nvgDelete(nanoVG);
	}

	public void loop() {

		ClientBase.getRenderer().getGpuTimer().startGPUTimer();

		//Tell the tick manager the frame time change
		tickManager.apply(getDeltaTimeInMillis());

		//tick as many times as needed
		while (tickManager.hasTick()) {
			ClientBase.getInstance().tick(getDeltaTimeInMillis());
		}

		if (ClientBase.getWindow().isResized()) {
			ClientBase.getWindow().updateDisplay();
			for (Entity matchingEntity : getManager().getMatchingEntities(ComponentFilter.builder().oneOf(CameraComponent.class).build())) {
				matchingEntity.getComponent(CameraComponent.class).updateProjectionMatrix();
			}
		}

		ClientBase.getWindow().getInputListener().update();
	}

	public void push() {

		var window = ClientBase.getWindow();
		int width = (int) (window.getEffectiveWidth());
		int height = (int) (window.getEffectiveHeight());

		var screenHandler = ClientBase.getInstance().getScreenHandler();

		screenHandler.update();

		NanoVG.nvgBeginFrame(getNanoVG(), width, height, Math.max(window.getContentScaleX(), window.getContentScaleY()));
		screenHandler.draw(getNanoVG());
		NanoVG.nvgEndFrame(getNanoVG());

		if (lastDrawBufferMode != drawBufferMode || lastPolygonMode != polygonMode) {
			glPolygonMode(drawBufferMode, polygonMode);
		}
		lastDrawBufferMode = drawBufferMode;
		lastPolygonMode = polygonMode;

		ClientBase.getRenderer().getGpuTimer().stopGPUTimer(3);
		glfwSwapBuffers(ClientBase.getWindow().getID());
		ClientBase.getWindow().getInputListener().resetDeltas();
		glfwPollEvents();
	}

	public static void setDrawBufferMode(DrawBufferMode mode) {
		drawBufferMode = mode.getMode();
	}

	public static void setBeginMode(PolygonMode mode) {
		polygonMode = mode.getMode();
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}

	enum DrawBufferMode {

		NONE(GL11.GL_NONE),
		FRONT_LEFT(GL11.GL_FRONT_LEFT),
		FRONT_RIGHT(GL11.GL_FRONT_RIGHT),
		BACK_LEFT(GL11.GL_BACK_LEFT),
		BACK_RIGHT(GL11.GL_BACK_RIGHT),
		FRONT(GL11.GL_FRONT),
		BACK(GL11.GL_BACK),
		LEFT(GL11.GL_LEFT),
		RIGHT(GL11.GL_RIGHT),
		FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK),
		AUX0(GL11.GL_AUX0),
		AUX1(GL11.GL_AUX1),
		AUX2(GL11.GL_AUX2),
		AUX3(GL11.GL_AUX3);

		final int bufferMode;

		DrawBufferMode(int mode) {
			this.bufferMode = mode;
		}

		public int getMode() {
			return bufferMode;
		}
	}

	public enum PolygonMode {

		POINT(GL_POINT),
		LINE(GL_LINE),
		FILL(GL_FILL);

		final int polygonMode;

		PolygonMode(int mode) {
			this.polygonMode = mode;
		}

		public int getMode() {
			return polygonMode;
		}
	}

	public Manager getManager() {
		return ClientBase.getInstance().getManager();
	}

	public ShaderHandler getShaderHandler() {
		return shaderHandler;
	}

	public GPUTimer getGpuTimer() {
		return gpuTimer;
	}

	public long getNanoVG() {
		return nanoVG;
	}

	public Capabilities getCapabilities() {
		return capabilities;
	}
}
