package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.events.HandleEvent;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class UICanvas extends UIRenderableElement {

	Window window;
	List<UIContainer> containers;
	Matrix4f translationMatrix;

	public UICanvas(Window window) {
		super();
		this.window = window;
		this.style = new UIStyle();
		this.containers = new ArrayList<>();
		this.translationMatrix = new Matrix4f();
		ClientBase.instance.addEventHandler(this);
	}

	public void addContainer(UIContainer container) {
		container.setParent(this);
		containers.add(container);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@HandleEvent(WindowResizeEvent.EVENT)
	public void onWindowResize(WindowResizeEvent event) {
		this.needsUpdate = true;
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
			this.needsUpdate = false;
			for (UIContainer container : containers) {
				container.update();
			}
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

	public UIStyle getStyle() {
		return style;
	}

	public List<UIContainer> getContainers() {
		return containers;
	}
}
