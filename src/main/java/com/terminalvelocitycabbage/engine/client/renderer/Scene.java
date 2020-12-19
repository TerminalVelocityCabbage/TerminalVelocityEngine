package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.util.SceneObjectHandler;

import java.util.List;

public abstract class Scene {

	public SceneObjectHandler objectHandler;

	public Scene() {
		this.objectHandler = new SceneObjectHandler();
	}

	public abstract void init();

	public abstract void update(long deltaTime);

	public abstract void destroy();

	public <T extends EmptyGameObject> List<T> getObjectsOfType(Class<T> type) {
		return objectHandler.getAllOfType(type);
	}

}
