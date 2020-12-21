package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.events.HandleEvent;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;

import java.util.ArrayList;
import java.util.List;

public class Canvas extends UIRenderableElement {

	Window window;
	List<Container> containers;

	public Canvas(Window window) {
		super(new Style());
		this.window = window;
		this.containers = new ArrayList<>();
		ClientBase.instance.addEventHandler(this);
	}

	public void addContainer(Container container) {
		container.setParent(this);
		container.zIndex = -1;
		containers.add(container);
		container.bind();
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@HandleEvent(WindowResizeEvent.EVENT)
	public void onWindowResize(WindowResizeEvent event) {
		queueUpdate();
	}

	@Override
	public void update() {
		if (needsUpdate) {
			rectangle.vertices[0].setXYZ(-1 + style.margin.left.getUnitizedValue(window.width()) - ((float)style.borderThickness / window.width()), 1 - style.margin.top.getUnitizedValue(window.height()) + ((float)style.borderThickness / window.height()), zIndex);
			rectangle.vertices[1].setXYZ(-1 + style.margin.left.getUnitizedValue(window.width()) - ((float)style.borderThickness / window.width()), -1 + style.margin.bottom.getUnitizedValue(window.height()) - ((float)style.borderThickness / window.height()), zIndex);
			rectangle.vertices[2].setXYZ(1 - style.margin.right.getUnitizedValue(window.width()) + ((float)style.borderThickness / window.width()), -1 + style.margin.bottom.getUnitizedValue(window.height()) - ((float)style.borderThickness / window.height()), zIndex);
			rectangle.vertices[3].setXYZ(1 - style.margin.right.getUnitizedValue(window.width()) + ((float)style.borderThickness / window.width()), 1 - style.margin.top.getUnitizedValue(window.height()) + ((float)style.borderThickness / window.height()), zIndex);
			this.width = (int)((rectangle.vertices[3].getXYZ()[0] - rectangle.vertices[0].getXYZ()[0]) / 2 * window.width()) - (style.borderThickness * 2);
			this.height = (int)((rectangle.vertices[3].getXYZ()[1] - rectangle.vertices[2].getXYZ()[1]) / 2 * window.height()) - (style.borderThickness * 2);
			rectangle.update(translationMatrix.identity());
			for (Container container : containers) {
				container.update();
			}
			this.needsUpdate = false;
		}
	}

	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public void queueUpdate() {
		super.queueUpdate();
		containers.forEach(UIRenderableElement::queueUpdate);
	}

	public Window getWindow() {
		return window;
	}

	public List<Container> getContainers() {
		return containers;
	}

	@Override
	public void destroy() {
		super.destroy();
		for (UIRenderableElement element : containers) {
			element.destroy();
		}
	}
}
