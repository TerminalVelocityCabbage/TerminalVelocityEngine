package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

public class SceneHandler {

	Map<String, Scene> sceneHandler;
	String activeScene;

	public SceneHandler() {
		this.sceneHandler = new HashMap<>();
		activeScene = null;
	}

	public void addScene(String name, Scene scene) {
		sceneHandler.put(name, scene);
	}

	public Scene getScene(String name) {
		if (!sceneHandler.containsKey(name)) {
			Log.crash("Tried to get a scene with unregistered key " + name, new RuntimeException("Could not find a scene with that key"));
		}
		return sceneHandler.get(name);
	}

	public Scene getActiveScene() {
		return sceneHandler.get(activeScene);
	}

	public void loadScene(String name) {
		if (sceneHandler.containsKey(name)) {
			if (activeScene != null && sceneHandler.containsKey(activeScene)) {
				sceneHandler.get(activeScene).destroy();
			}
			activeScene = name;
			sceneHandler.get(name).init(ClientBase.getWindow());
			ClientBase.getWindow().focus();
		} else {
			Log.crash("Scene Load Exception", new RuntimeException("Could not load scene " + name + " not present in scene handler."));
		}
	}

	public boolean isActive(String testableScene) {
		return activeScene.equalsIgnoreCase(testableScene);
	}

	public void cleanup() {
		for (Scene scene : sceneHandler.values()) {
			scene.destroy();
		}
	}

	public boolean hasScene(String scene) {
		return sceneHandler.containsKey(scene);
	}

}
