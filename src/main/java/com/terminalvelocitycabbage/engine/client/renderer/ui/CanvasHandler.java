package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
			activeCanvases.add(name);
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

	public void tick(double posX, double posY) {

		//Call hoverable events if the element is being hovered over
		for (UIRenderableElement element : getCanvasesAt(posX, posY)) {
			element.callHoverable();
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

	public List<UIRenderableElement> getCanvasesAt(double x, double y) {
		List<UIRenderableElement> inCanvases = new ArrayList<>();
		Canvas currCanvas;
		Vertex[] currVertices;
		for (String currID : activeCanvases) {
			currCanvas = canvases.get(currID);
			currVertices = currCanvas.rectangle.vertices;
			if (testPosition(currVertices, x, y)) {
				inCanvases.add(currCanvas);
			} else {
				continue;
			}
			for (Container element : currCanvas.containers) {
				currVertices = element.rectangle.vertices;
				if (testPosition(currVertices, x, y)) {
					inCanvases.add(element);
				}
			}
		}
		return inCanvases;
	}

	private void recursiveOnElement(List<Container> container, Consumer<UIRenderableElement> consumer) {
		//TODO when containers have elements and other containers within them
	}

	private boolean testPosition(Vertex[] currVertices, double x, double y) {
		if (currVertices[0].getX() < x && currVertices[2].getX() > x && currVertices[1].getY() < y && currVertices[0].getY() > y) {
			return true;
		}
		return false;
	}

}
