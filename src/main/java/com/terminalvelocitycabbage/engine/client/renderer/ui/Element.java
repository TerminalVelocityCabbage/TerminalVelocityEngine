package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.debug.Log;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.UIDimension.Unit.PERCENT;

public class Element extends UIRenderableElement {

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
			//Get boundaries of parent
			float originXMin = parent.rectangle.vertices[0].getX();
			float originXMax = parent.rectangle.vertices[2].getX();
			float originYMin = parent.rectangle.vertices[1].getY();
			float originYMax = parent.rectangle.vertices[0].getY();

			//Window dimensions
			int windowWidth = getCanvas().window.width();
			int windowHeight = getCanvas().window.height();

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
			float xOffset = width.unit.equals(PERCENT) ? width.value / 100f * containerWidth : width.getUnitizedValue(windowWidth);
			float yOffset = height.unit.equals(PERCENT) ? height.value / 100f * containerHeight : height.getUnitizedValue(windowHeight);

			//Give the element it's dimensions
			leftX -= xOffset / 2f;
			rightX += xOffset / 2f;
			bottomY -= yOffset / 2f;
			topY += yOffset / 2f;

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
