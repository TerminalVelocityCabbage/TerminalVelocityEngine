package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.events.HandleEvent;
import com.terminalvelocitycabbage.engine.events.client.WindowResizeEvent;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PIXELS;

public class UICanvas extends UIRenderableElement {

	Window window;
	public UIStyle style;
	UIDimension marginLeft;
	UIDimension marginRight;
	UIDimension marginTop;
	UIDimension marginBottom;
	List<UIContainer> containers;
	Matrix4f translationMatrix;

	public UICanvas(Window window) {
		super();
		this.window = window;
		this.style = UIStyle.builder().build();
		marginLeft = new UIDimension(0, PIXELS);
		marginRight = new UIDimension(0, PIXELS);
		marginTop = new UIDimension(0, PIXELS);
		marginBottom = new UIDimension(0, PIXELS);
		this.containers = new ArrayList<>();
		this.translationMatrix = new Matrix4f();
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
			rectangle.vertices[0].setXYZ(-1 + marginLeft.getUnitizedValue(window.width()) - (style.borderThickness / window.width()), 1 - marginTop.getUnitizedValue(window.height()) + (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[1].setXYZ(-1 + marginLeft.getUnitizedValue(window.width()) - (style.borderThickness / window.width()), -1 + marginBottom.getUnitizedValue(window.height()) - (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[2].setXYZ(1 - marginRight.getUnitizedValue(window.width()) + (style.borderThickness / window.width()), -1 + marginBottom.getUnitizedValue(window.height()) - (style.borderThickness / window.height()), zIndex);
			rectangle.vertices[3].setXYZ(1 - marginRight.getUnitizedValue(window.width()) + (style.borderThickness / window.width()), 1 - marginTop.getUnitizedValue(window.height()) + (style.borderThickness / window.height()), zIndex);
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

	public UIDimension getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(UIDimension marginLeft) {
		this.marginLeft = marginLeft;
	}

	public UIDimension getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(UIDimension marginRight) {
		this.marginRight = marginRight;
	}

	public UIDimension getMarginTop() {
		return marginTop;
	}

	public void setMarginTop(UIDimension marginTop) {
		this.marginTop = marginTop;
	}

	public UIDimension getMarginBottom() {
		return marginBottom;
	}

	public void setMarginBottom(UIDimension marginBottom) {
		this.marginBottom = marginBottom;
	}

	public void setMarginUnit(UIDimension.Unit unit) {
		setMarginUnits(unit, unit, unit, unit);
	}

	public void setMarginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
		this.marginLeft.unit = left;
		this.marginRight.unit = right;
		this.marginTop.unit = top;
		this.marginBottom.unit = bottom;
	}

	public void setMargins(int value) {
		setMargins(value, value, value, value);
	}

	public void setMargins(int left, int right, int top, int bottom) {
		this.marginLeft.value = left;
		this.marginRight.value = right;
		this.marginTop.value = top;
		this.marginBottom.value = bottom;
	}

	public List<UIContainer> getContainers() {
		return containers;
	}
}
