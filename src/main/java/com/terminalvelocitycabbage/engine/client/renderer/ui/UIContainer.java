package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.Arrays;

public class UIContainer extends UIRenderableElement {

	public UIDimension width;
	public UIDimension height;
	public UIAnchor anchorPoint;
	public UIStyle style;
	public UIRenderableElement parent;

	public UIContainer(UIDimension width, UIDimension height, UIAnchor anchorPoint, UIStyle style) {
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

	public UIAnchor getAnchorPoint() {
		return anchorPoint;
	}

	public UIStyle getStyle() {
		return style;
	}

	@Override
	public void update() {
		if (needsUpdate) {

			//Get parent's pixel dimensions
			int containerWidth = parent.getWidth();
			int containerHeight = parent.getHeight();

			//Get boundaries of parent
			float originXMin = parent.rectangle.vertices[0].getX();
			float originYMin = parent.rectangle.vertices[1].getY();
			float originXMax = parent.rectangle.vertices[2].getX();
			float originYMax = parent.rectangle.vertices[0].getY();

			//Window dimensions
			int windowWidth = getCanvas().window.width();
			int windowHeight = getCanvas().window.height();

			//Unit dimensions
			float uWidth = width.getUnitizedValue(windowWidth);
			float uHeight = height.getUnitizedValue(windowHeight);

			//Store potential locations
			float leftX = 0;
			float rightX = 0;
			float topY = 0;
			float bottomY = 0;

			//TODO these 6 if statements just implement the default anchor directions, implement those too
			//TODO account for margins
			//If it's x alignment is left
			if (anchorPoint.anchorPoint.xPos == -1) {
				leftX = originXMin;
				rightX = originXMin + uWidth;
			}
			//If the x alignment is centered
			if (anchorPoint.anchorPoint.xPos == 0) {
				leftX = ((originXMax + originXMin) / 2) - (uWidth / 2);
				rightX = ((originXMax + originXMin) / 2) + (uWidth / 2);
			}
			//If the x alignment is right
			if (anchorPoint.anchorPoint.xPos == 1) {
				leftX = originXMax - uWidth;
				rightX = originXMax;
			}
			//If the y alignment is bottom
			if (anchorPoint.anchorPoint.yPos == - 1) {
				bottomY = originYMin;
				topY = originYMin + uHeight;
			}
			//If the y alignment is centered
			if (anchorPoint.anchorPoint.yPos == 0) {
				bottomY = ((originYMax + originYMin) / 2) - (uHeight / 2);
				topY = ((originYMax + originYMin) / 2) + (uHeight / 2);
			}
			//If the y alignment is top
			if (anchorPoint.anchorPoint.yPos == 1) {
				bottomY = originYMax - uHeight;
				topY = originYMax;
			}

			//Set the vertexes based on the calculated positions
			rectangle.vertices[0].setXYZ(leftX, topY, zIndex);
			rectangle.vertices[1].setXYZ(leftX, bottomY, zIndex);
			rectangle.vertices[2].setXYZ(rightX, bottomY, zIndex);
			rectangle.vertices[3].setXYZ(rightX, topY, zIndex);

			//Update the data that gets passed to the gpu
			rectangle.update(translationMatrix.identity());

			//Complete this update
			needsUpdate = false;
		}
	}
}
