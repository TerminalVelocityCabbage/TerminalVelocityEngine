package com.terminalvelocitycabbage.engine.client.renderer.ui;

public class UIContainer extends UIRenderableElement {

	public UIDimension width;
	public UIDimension height;
	public UIAnchor anchorPoint;
	public UIRenderableElement parent;

	public UIContainer(UIDimension width, UIDimension height, UIAnchor anchorPoint, UIStyle style) {
		super(style);
		this.width = width;
		this.height = height;
		this.anchorPoint = anchorPoint;
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

	public UIStyle getStyle() {
		return style;
	}

	@Override
	public void update() {
		if (needsUpdate) {

			//Get boundaries of parent
			float originXMin = parent.rectangle.vertices[0].getX();
			float originYMin = parent.rectangle.vertices[1].getY();
			float originXMax = parent.rectangle.vertices[2].getX();
			float originYMax = parent.rectangle.vertices[0].getY();

			//Window dimensions
			int windowWidth = getCanvas().window.width();
			int windowHeight = getCanvas().window.height();

			//Get parent's unit dimensions
			float uContainerWidth = (float)parent.getWidth() / windowWidth;
			float uContainerHeight = (float)parent.getHeight() / windowHeight;

			//Container center
			float containerCenterX = (originXMin + originXMax) / 2;
			float containerCenterY = (originYMin + originYMax) / 2;

			//Store offsets for anchorPosition center
			float xOffset = anchorPoint.anchorPoint.xPos * (uContainerWidth);
			float yOffset = anchorPoint.anchorPoint.yPos * (uContainerHeight);

			//Unit dimensions
			float uWidth = width.getUnitizedValue(windowWidth);
			float uHeight = height.getUnitizedValue(windowHeight);

			//Place all vertices at the center of the parent
			float leftX = containerCenterX;
			float rightX = containerCenterX;
			float topY = containerCenterY;
			float bottomY = containerCenterY;

			//Store offsets for anchor direction
			float xDirOffset = anchorPoint.anchorDirection.xDirection * (uWidth / 2);
			float yDirOffset = anchorPoint.anchorDirection.yDirection * (uHeight / 2);

			//Give the container dimensions
			leftX -= uWidth / 2;
			rightX += uWidth / 2;
			bottomY -= uHeight / 2;
			topY += uHeight / 2;

			//Apply margins
			leftX += style.margin.left.getUnitizedValue(windowWidth);
			rightX -= style.margin.right.getUnitizedValue(windowWidth);
			bottomY += style.margin.bottom.getUnitizedValue(windowHeight);
			topY -= style.margin.top.getUnitizedValue(windowHeight);

			//Move this box to be centered on the anchor point
			leftX += xOffset;
			rightX += xOffset;
			bottomY += yOffset;
			topY += yOffset;

			//Move this box to go in it's anchor direction
			leftX += xDirOffset;
			rightX += xDirOffset;
			bottomY += yDirOffset;
			topY += yDirOffset;

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
