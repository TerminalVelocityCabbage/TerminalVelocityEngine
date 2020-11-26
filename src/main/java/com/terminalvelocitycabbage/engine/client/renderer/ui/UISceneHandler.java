package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.ui.elements.UICanvas;

import java.util.HashMap;

public abstract class UISceneHandler {

	Window window;
	HashMap<String, UICanvas> scenes = new HashMap<>();

	public UISceneHandler(Window window) {
		this.window = window;
	}

	public void create(String name, UICanvas canvas) {
		if (scenes.containsKey(name)) {
			throw new RuntimeException("Canvas with name " + name + " already exists in this UI Scene Handler.");
		}
		scenes.put(name, canvas.build());
	}

	public void update() {
		if (window.isResized()) {
			for (UICanvas canvas : scenes.values()) {
				canvas.width = window.width();
				canvas.height = window.height();
				canvas.update();
			}
		}
	}

	public void render(String scene) {
		if (scenes.containsKey(scene)) {
			scenes.get(scene).render();
		} else {
			throw new RuntimeException("No scene found with canvas name " + scene);
		}
	}

	public void show(String scene) {
		if (scenes.containsKey(scene)) {
			scenes.get(scene).show();
		} else {
			throw new RuntimeException("No scene found with canvas name " + scene);
		}
	}

	public void hide(String scene) {
		if (scenes.containsKey(scene)) {
			scenes.get(scene).hide();
		} else {
			throw new RuntimeException("No scene found with canvas name " + scene);
		}
	}

	public void cleanup() {
		for (UICanvas canvas : scenes.values()) {
			canvas.destroy();
		}
	}

}
