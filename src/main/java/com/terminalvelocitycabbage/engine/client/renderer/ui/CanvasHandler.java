package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasHandler {

	Map<String, Canvas> canvases;
	List<String> activeCanvases;

	public CanvasHandler() {
		this.canvases = new HashMap<>();
		this.activeCanvases = new ArrayList<>();
	}

	public void addCanvas(String name, Canvas canvas) {
		if (!canvases.containsKey(name)) {
			this.canvases.put(name, canvas);
		} else {
			throw new RuntimeException("Could not add duplicate entry " + name + " to canvas handler.");
		}
	}

	public void enableCanvas(String name) {
		if (!activeCanvases.contains(name)) {
			if (canvases.containsKey(name)) {
				activeCanvases.add(name);
			} else {
				Log.warn("Tried to enable unregistered canvas " + name);
			}
		}
	}

	public void hideCanvas(String name) {
		if (activeCanvases.contains(name)) {
			activeCanvases.remove(name);
		}
	}

	public Canvas getCanvas(String name) {
		if (canvases.containsKey(name)) {
			return canvases.get(name);
		} else {
			throw new RuntimeException("could not get canvas for name " + name);
		}
	}

	public boolean isEnabled(String name) {
		return activeCanvases.contains(name);
	}

	public void cleanup() {
		for (Canvas canvas : canvases.values()) {
			canvas.destroy();
		}
	}

}
