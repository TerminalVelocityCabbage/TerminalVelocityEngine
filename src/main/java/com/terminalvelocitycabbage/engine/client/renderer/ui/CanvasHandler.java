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

		//Call hoverable events if the element is being hovered over
		for (UIRenderable element : getCanvasesAt(posX, posY)) {
			element.callHoverable();
			if (leftClick) {
				element.callClick();
				element.callDoubleCLick(timeSinceLastClick);
			}
			if (rightClick) {
				element.callRightClick();
			}
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

	public List<UIRenderable> getCanvasesAt(double x, double y) {
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
		if (currVertices[0].getX() < x && currVertices[2].getX() > x && currVertices[1].getY() < y && currVertices[0].getY() > y) {
			return true;
		}
		return false;
	}

	public Collection<Canvas> getCanvases() {
		return canvases.values();
	}

}
