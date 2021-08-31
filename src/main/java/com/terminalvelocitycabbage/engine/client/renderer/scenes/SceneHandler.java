package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
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

	public Scene getActiveScene() {
		return sceneHandler.get(activeScene);
	}

	public void loadScene(String name) {
		if (sceneHandler.containsKey(name)) {
			if (activeScene != null && sceneHandler.containsKey(activeScene)) {
				sceneHandler.get(activeScene).destroy();
			}
			activeScene = name;
			sceneHandler.get(name).init(Renderer.getWindow());
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
