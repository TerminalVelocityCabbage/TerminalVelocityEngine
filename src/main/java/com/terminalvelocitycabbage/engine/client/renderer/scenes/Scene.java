package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.renderer.components.Camera;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.util.SceneObjectHandler;

import java.util.List;

public abstract class Scene {

	Camera camera;
	public SceneObjectHandler objectHandler;

	public Scene(Camera camera) {
		this.objectHandler = new SceneObjectHandler();
		this.camera = camera;
	}

	public abstract void init();

	public abstract void update(float deltaTime);

	public abstract void destroy();

	public Camera getCamera() {
		return camera;
	}

	public <T extends EmptyGameObject> List<T> getObjectsOfType(Class<T> type) {
		return objectHandler.getAllOfType(type);
	}

}
