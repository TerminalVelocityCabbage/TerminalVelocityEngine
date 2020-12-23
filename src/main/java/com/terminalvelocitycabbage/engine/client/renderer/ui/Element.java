package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import com.terminalvelocitycabbage.engine.debug.Log;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;

public class Element extends UIRenderable {

	public UIDimension width;
	public UIDimension height;
	public Container parent;

	//TODO make this a stylable text
	public String innerText;

	public Element(String text, UIDimension width, UIDimension height, Style style) {
		super(style);
		this.width = width;
		this.height = height;
		this.innerText = text;
	}

	@Override
	public void update() {

		if (needsUpdate) {

			//Window dimensions
			int windowWidth = getCanvas().window.width();
			int windowHeight = getCanvas().window.height();

			//Get boundaries of parent
			float originXMin = parent.rectangle.vertices[0].getX() + ((float)parent.style.getBorderThickness() / windowWidth * 2);
			float originXMax = parent.rectangle.vertices[2].getX() - ((float)parent.style.getBorderThickness() / windowWidth * 2);
			float originYMin = parent.rectangle.vertices[1].getY() + ((float)parent.style.getBorderThickness() / windowHeight * 2);
			float originYMax = parent.rectangle.vertices[0].getY() - ((float)parent.style.getBorderThickness() / windowHeight * 2);

			//Container dimensions
			float containerWidth = originXMax - originXMin;
			float containerHeight = originYMax - originYMin;

			//Container center
			float containerCenterX = (originXMin + originXMax) / 2f;
			float containerCenterY = (originYMin + originYMax) / 2f;

			//Create variables to store vertex Positions in at the center of the parent
			float leftX = containerCenterX;
			float rightX = containerCenterX;
			float topY = containerCenterY;
			float bottomY = containerCenterY;

			//Create temp width and height vars in case of a responsive layout
			float xOffset = width.getUnitDirect().equals(PERCENT) ? width.getValueDirect() / 100f * containerWidth : width.getUnitizedValue(windowWidth);
			float yOffset = height.getUnitDirect().equals(PERCENT) ? height.getValueDirect() / 100f * containerHeight : height.getUnitizedValue(windowHeight);

			//Give the element it's dimensions
			leftX -= xOffset / 2f;
			rightX += xOffset / 2f;
			bottomY -= yOffset / 2f;
			topY += yOffset / 2f;

			//Translate the element to it's start point
			leftX += parent.horizontalAlignment.getStart() * containerWidth / 2;
			rightX += parent.horizontalAlignment.getStart() * containerWidth / 2;
			topY += parent.verticalAlignment.getStart() * containerHeight / 2;
			bottomY += parent.verticalAlignment.getStart() * containerHeight / 2;

			//Offset them in the opposite of the start to make them position with their corner/edge in the right spot
			leftX += (xOffset / 2) * (parent.horizontalAlignment.getStart() * -1);
			rightX += (xOffset / 2) * (parent.horizontalAlignment.getStart() * -1);
			topY += (yOffset / 2) * (parent.verticalAlignment.getStart() * -1);
			bottomY += (yOffset / 2) * (parent.verticalAlignment.getStart() * -1);

			//Get dimensions of previous elements
			float prevElementWidths = parent.getWidthOfElements(0, this.getPosition());
			float prevElementHeights = parent.getHeightOfElements(0, this.getPosition());

			//Move this element based on the dimensions of elements before this one
			leftX += parent.alignmentDirection.equals(Alignment.Direction.HORIZONTAL) ? prevElementWidths * -parent.horizontalAlignment.getStart() : 0;
			rightX += parent.alignmentDirection.equals(Alignment.Direction.HORIZONTAL) ? prevElementWidths * -parent.horizontalAlignment.getStart() : 0;
			topY += parent.alignmentDirection.equals(Alignment.Direction.VERTICAL) ? prevElementHeights * -parent.verticalAlignment.getStart() : 0;
			bottomY += parent.alignmentDirection.equals(Alignment.Direction.VERTICAL) ? prevElementHeights * -parent.verticalAlignment.getStart() : 0;

			//Set the vertexes based on the calculated positions
			rectangle.vertices[0].setXYZ(leftX, topY, zIndex);
			rectangle.vertices[1].setXYZ(leftX, bottomY, zIndex);
			rectangle.vertices[2].setXYZ(rightX, bottomY, zIndex);
			rectangle.vertices[3].setXYZ(rightX, topY, zIndex);
		}

		//Update the data that gets passed to the gpu
		rectangle.update(translationMatrix.identity());

		//Complete this update
		needsUpdate = false;
	}

	public int getPosition() {
		return parent.elements.indexOf(this);
	}

	public Canvas getCanvas() {
		return parent.getCanvas();
	}

	public void setParent(Container parent) {
		this.parent = parent;
	}

	@Override
	public Element onHover(Runnable runnable) {
		return (Element) super.onHover(runnable);
	}

	@Override
	public Element onClick(Runnable runnable) {
		return (Element) super.onClick(runnable);
	}

	@Override
	public Element onRightClick(Runnable runnable) {
		return (Element) super.onRightClick(runnable);
	}

	@Override
	public Element onDoubleClick(short tickTime, Runnable runnable) {
		return (Element) super.onDoubleClick(tickTime, runnable);
	}

	@Override
	public void render() {
		super.render();
		if (false) {
			Log.warn("------------------------");
			Log.info(rectangle.vertices[0].getX() + " " + rectangle.vertices[0].getY());
			Log.info(rectangle.vertices[1].getX() + " " + rectangle.vertices[1].getY());
			Log.info(rectangle.vertices[2].getX() + " " + rectangle.vertices[2].getY());
			Log.info(rectangle.vertices[3].getX() + " " + rectangle.vertices[3].getY());
		}
	}
}
