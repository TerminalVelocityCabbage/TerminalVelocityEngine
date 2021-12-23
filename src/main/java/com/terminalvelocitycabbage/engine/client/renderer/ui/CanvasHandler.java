package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.*;
import java.util.stream.Collectors;

public class CanvasHandler {

	Map<String, Canvas> canvases;

	public CanvasHandler() {
		this.canvases = new HashMap<>();
	}

	public void addCanvas(String name, Canvas canvas) {
		if (!canvases.containsKey(name)) {
			this.canvases.put(name, canvas);
			canvas.queueUpdate();
		} else {
			Log.crash("Canvas Addition Error", new RuntimeException("Could not add duplicate entry " + name + " to canvas handler."));
		}
	}

	public void showCanvas(String name) {
		if (canvases.containsKey(name)) {
			canvases.get(name).activate();
		} else {
			Log.warn("Tried to enable unregistered canvas " + name);
		}
	}

	public void hideCanvas(String name) {
		if (canvases.containsKey(name)) {
			canvases.get(name).deactivate();
		} else {
			Log.warn("Tried to disable unregistered canvas " + name);
		}
	}

	public Canvas getCanvas(String name) {
		if (canvases.containsKey(name)) {
			return canvases.get(name);
		} else {
			Log.crash("Canvas Retrieval Error", new RuntimeException("could not get canvas for name " + name));
			return null;
		}
	}

	public void tick(double posX, double posY, boolean leftClick, boolean rightClick, int timeSinceLastClick) {
		for (Canvas canvas : getActiveCanvases()) {
			canvas.update();
		}
		getActiveCanvases().forEach(canvas -> canvas.getAllChildren().forEach(element -> {

			if (testPosition(element.vertices, posX, posY)) {
				element.callHoverable();
				if (leftClick) {
					element.callClick();
					element.callDoubleCLick(timeSinceLastClick);
				}
				if (rightClick) {
					element.callRightClick();
				}
			} else {
				if (element.lastHover) {
					element.callUnHover();
					element.lastHover = false;
				}
			}
		}));
	}

	public void cleanup() {
		for (Canvas canvas : canvases.values()) {
			canvas.model.destroy();
		}
	}

	public List<UIRenderable> getElementsAt(double x, double y) {
		List<UIRenderable> inCanvases = new ArrayList<>();
		Vertex[] currVertices;
		for (Canvas canvas : getActiveCanvases()) {
			currVertices = canvas.vertices;
			if (testPosition(currVertices, x, y)) {
				inCanvases.add(canvas);
				for (Container element : canvas.containers) {
					currVertices = element.vertices;
					if (testPosition(currVertices, x, y)) {
						inCanvases.add(element);
					}
				}
			}
		}
		return inCanvases;
	}

	private boolean testPosition(Vertex[] currVertices, double x, double y) {
		return currVertices[0].getX() < x && currVertices[2].getX() > x && currVertices[1].getY() < y && currVertices[0].getY() > y;
	}

	public List<Canvas> getActiveCanvases() {
		return getCanvases().stream().filter(canvas -> canvas.active).collect(Collectors.toList());
	}

	public Collection<Canvas> getCanvases() {
		return canvases.values();
	}

}
