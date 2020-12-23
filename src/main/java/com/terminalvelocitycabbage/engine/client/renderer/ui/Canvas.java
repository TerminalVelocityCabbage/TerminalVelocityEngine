package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;
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
		container.zIndex = zIndex - 1;
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
			rectangle.vertices[0].setXYZ(-1 + style.getMargin().left().getUnitizedValue(window.width()) - ((float)style.getBorderThickness() / window.width()), 1 - style.getMargin().top().getUnitizedValue(window.height()) + ((float)style.getBorderThickness() / window.height()), zIndex);
			rectangle.vertices[1].setXYZ(-1 + style.getMargin().left().getUnitizedValue(window.width()) - ((float)style.getBorderThickness() / window.width()), -1 + style.getMargin().bottom().getUnitizedValue(window.height()) - ((float)style.getBorderThickness() / window.height()), zIndex);
			rectangle.vertices[2].setXYZ(1 - style.getMargin().right().getUnitizedValue(window.width()) + ((float)style.getBorderThickness() / window.width()), -1 + style.getMargin().bottom().getUnitizedValue(window.height()) - ((float)style.getBorderThickness() / window.height()), zIndex);
			rectangle.vertices[3].setXYZ(1 - style.getMargin().right().getUnitizedValue(window.width()) + ((float)style.getBorderThickness() / window.width()), 1 - style.getMargin().top().getUnitizedValue(window.height()) + ((float)style.getBorderThickness() / window.height()), zIndex);
			rectangle.update(translationMatrix.identity());
			for (Container container : containers) {
				container.queueUpdate();
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

	@Override
	public Canvas onHover(Runnable runnable) {
		return (Canvas) super.onHover(runnable);
	}

	@Override
	public Canvas onClick(Runnable runnable) {
		return (Canvas) super.onClick(runnable);
	}

	@Override
	public Canvas onRightClick(Runnable runnable) {
		return (Canvas) super.onRightClick(runnable);
	}

	@Override
	public Canvas onDoubleClick(short tickTime, Runnable runnable) {
		return (Canvas) super.onDoubleClick(tickTime, runnable);
	}

	public List<UIRenderableElement> getAllChildren() {

		//Add all of this canvas' containers to a list
		List<Container> allContainers = new ArrayList<>(containers);
		//For each of those containers recursively add all containers in that tree
		containers.forEach(container -> allContainers.addAll(container.getAllContainers()));

		//Add all of the containers in this canvas' tree
		List<UIRenderableElement> elements = new ArrayList<>(allContainers);
		//Add all elements of the containers to the list of children
		allContainers.forEach(container -> elements.addAll(container.getElements()));
		return elements;
	}
}
