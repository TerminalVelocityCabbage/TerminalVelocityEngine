package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;

import java.util.List;

public abstract class Scene {

	Camera camera;
	InputHandler inputHandler;
	public SceneObjectHandler objectHandler;

	public Scene(Camera camera, InputHandler inputHandler) {
		this.camera = camera;
		this.inputHandler = inputHandler;
		this.objectHandler = new SceneObjectHandler();
	}

	public void init(Window window) {
		inputHandler.init(window);
	}

	public abstract void tick(float deltaTime);

	public abstract void destroy();

	public Camera getCamera() {
		return camera;
	}

	public <T extends EmptyGameObject> List<T> getObjectsOfType(Class<T> type) {
		return objectHandler.getAllOfType(type);
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}
}
