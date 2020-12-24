package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.*;
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
			canvas.bind();
			canvas.queueUpdate();
		} else {
			throw new RuntimeException("Could not add duplicate entry " + name + " to canvas handler.");
		}
	}

	public void enableCanvas(String name) {
		if (!activeCanvases.contains(name)) {
			if (isEnabled(name)) {
				activeCanvases.add(name);
			} else {
				Log.warn("Tried to enable unregistered canvas " + name);
			}
		}
	}

	public void hideCanvas(String name) {
		Log.info("hide");
		if (isEnabled(name)) {
			activeCanvases.remove(name);
		}
	}

	public Canvas getCanvas(String name) {
		if (isEnabled(name)) {
			return canvases.get(name);
		} else {
			throw new RuntimeException("could not get canvas for name " + name);
		}
	}

	public void tick(double posX, double posY, boolean leftClick, boolean rightClick, short timeSinceLastClick) {

		Log.error("tick");

		getCanvases().forEach(canvas -> canvas.getAllChildren().forEach(element -> {

			element.lastHover = false;

			if (testPosition(element.rectangle.vertices, posX, posY)) {
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

	public boolean isEnabled(String name) {
		return activeCanvases.contains(name);
	}

	public void cleanup() {
		for (Canvas canvas : canvases.values()) {
			canvas.destroy();
		}
	}

	public List<UIRenderable> getElementsAt(double x, double y) {
		List<UIRenderable> inCanvases = new ArrayList<>();
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

	private void recursiveOnElement(List<Container> container, Consumer<UIRenderable> consumer) {
		//TODO when containers have elements and other containers within them
	}

	private boolean testPosition(Vertex[] currVertices, double x, double y) {
		return currVertices[0].getX() < x && currVertices[2].getX() > x && currVertices[1].getY() < y && currVertices[0].getY() > y;
	}

	public Collection<Canvas> getCanvases() {
		return canvases.values();
	}

}
