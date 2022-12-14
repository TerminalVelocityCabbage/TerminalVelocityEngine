package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.Window;
import com.terminalvelocitycabbage.engine.ecs.Manager;

public abstract class Scene {

	Camera camera;
	InputHandler inputHandler;

	public Scene(Camera camera, InputHandler inputHandler) {
		this.camera = camera;
		this.inputHandler = inputHandler;
	}

	public void init(Window window) {
		inputHandler.init(window);
	}

	public abstract void tick(float deltaTime);

	public abstract void destroy();

	public Camera getCamera() {
		return camera;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public Manager getManager() {
		return ClientBase.getRenderer().getManager();
	}
}
