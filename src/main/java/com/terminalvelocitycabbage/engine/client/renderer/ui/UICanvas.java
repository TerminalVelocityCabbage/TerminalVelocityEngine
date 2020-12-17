package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class UICanvas extends UIRenderableElement {

	Window window;
	public UIStyle style;
	float marginLeft;
	float marginRight;
	float marginTop;
	float marginBottom;
	List<UIContainer> containers;
	Matrix4f translationMatrix;

	public UICanvas(Window window) {
		super();
		this.window = window;
		this.style = UIStyle.builder().build();
		this.setMargins(0, 0, 0, 0);
		this.containers = new ArrayList<>();
		this.translationMatrix = new Matrix4f();
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	@Override
	public void update() {
		if (needsUpdate) {
			rectangle.vertices[0].setXYZ(-1 + marginLeft - (style.borderThickness / window.width()), 1 - marginTop + (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[1].setXYZ(-1 + marginLeft - (style.borderThickness / window.width()), -1 + marginBottom - (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[2].setXYZ(1 - marginRight + (style.borderThickness / window.width()), -1 + marginBottom - (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[3].setXYZ(1 - marginRight + (style.borderThickness / window.width()), 1 - marginTop + (style.borderThickness / window.height()), zIndex);
			rectangle.update(translationMatrix.identity());
			this.needsUpdate = false;
		}
	}

	public Window getWindow() {
		return window;
	}

	public UIStyle getStyle() {
		return style;
	}

	public void setColor(float r, float g, float b, float opacity) {
		this.style.backgroundColor.set(r, g, b, opacity);
	}

	public void setMarginLeft(float marginLeft) {
		this.marginLeft = marginLeft;
	}

	public void setMarginRight(float marginRight) {
		this.marginRight = marginRight;
	}

	public void setMarginTop(float marginTop) {
		this.marginTop = marginTop;
	}

	public void setMarginBottom(float marginBottom) {
		this.marginBottom = marginBottom;
	}

	public float getMarginLeft() {
		return marginLeft;
	}

	public float getMarginRight() {
		return marginRight;
	}

	public float getMarginTop() {
		return marginTop;
	}

	public float getMarginBottom() {
		return marginBottom;
	}

	public void setMargins(float left, float right, float top, float bottom) {
		this.marginLeft = left;
		this.marginRight = right;
		this.marginTop = top;
		this.marginBottom = bottom;
	}

	public List<UIContainer> getContainers() {
		return containers;
	}
}
