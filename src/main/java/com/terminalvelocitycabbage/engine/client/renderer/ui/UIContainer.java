package com.terminalvelocitycabbage.engine.client.renderer.ui;

public class UIContainer extends UIRenderableElement {

	public float width;
	public float height;
	public UIAnchor anchorPoint;
	public UIStyle style;

	public UIContainer(float width, float height, UIAnchor anchorPoint, UIStyle style) {
		this.width = width;
		this.height = height;
		this.anchorPoint = anchorPoint;
		this.style = style;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public UIAnchor getAnchorPoint() {
		return anchorPoint;
	}

	public UIStyle getStyle() {
		return style;
	}

	@Override
	public void update() {

	}
}
