package com.terminalvelocitycabbage.engine.client.renderer.ui;

public class UIContainer extends UIRenderableElement {

	public int width;
	public int height;
	public UIAnchor anchorPoint;
	public UIStyle style;
	public UIRenderableElement parent;

	public UIContainer(int width, int height, UIAnchor anchorPoint, UIStyle style) {
		super();
		this.width = width;
		this.height = height;
		this.anchorPoint = anchorPoint;
		this.style = style;
	}

	public UICanvas getCanvas() {
		if (parent.isRoot()) {
			return (UICanvas)parent;
		} else {
			return ((UIContainer)parent).getCanvas();
		}
	}

	public void setParent(UIRenderableElement canvas) {
		this.parent = canvas;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
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
		if (needsUpdate) {

			int containerWidth = parent.getWidth();
			int containerHeight = parent.getHeight();

			//float originX = anchorPoint.anchorPoint.xPos + parent.rectangle.vertices[0].getXYZ()[0] - parent.style;
			float originY = anchorPoint.anchorPoint.yPos;

			//rectangle.vertices[0].setXYZ();
			//rectangle.vertices[1].setXYZ();
			//rectangle.vertices[2].setXYZ();
			//rectangle.vertices[3].setXYZ();
		}
	}
}
