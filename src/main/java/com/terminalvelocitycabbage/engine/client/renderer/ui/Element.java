package com.terminalvelocitycabbage.engine.client.renderer.ui;

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

		//Get boundaries of parent
		float originXMin = parent.rectangle.vertices[0].getX();
		float originYMin = parent.rectangle.vertices[1].getY();
		float originXMax = parent.rectangle.vertices[2].getX();
		float originYMax = parent.rectangle.vertices[0].getY();

		//Window dimensions
		int windowWidth = getCanvas().window.width();
		int windowHeight = getCanvas().window.height();

		//Get parent's unit dimensions
		float uContainerWidth = (float)parent.getWidth().getPixelValue(windowWidth) / windowWidth;
		float uContainerHeight = (float)parent.getHeight().getPixelValue(windowHeight) / windowHeight;

		//Get parent pixel dimensions
		int pxContainerWidth = parent.getWidth().getPixelValue(windowWidth);
		int pxContainerHeight = parent.getHeight().getPixelValue(windowHeight);

		//Create variables to store vertex Positions in
		float leftX = originXMin;
		float rightX = originXMax;
		float topY = originYMax;
		float bottomY = originYMin;

		//Offsets
		float xOffset = uContainerWidth - width.getUnitizedValue(pxContainerWidth);
		float yOffset = uContainerHeight - height.getUnitizedValue(pxContainerHeight);

		//Horizontal Opposite to start Positions
		if (parent.horizontalAlignment.start == -1) rightX -= xOffset;
		if (parent.horizontalAlignment.start == 1) leftX += xOffset;
		if (parent.horizontalAlignment.start == 0) {
			leftX += xOffset / 2;
			rightX -= xOffset / 2;
		}

		//Vertical Opposite to start Positions
		if (parent.verticalAlignment.start == -1) topY -= yOffset;
		if (parent.verticalAlignment.start == 1) bottomY += yOffset;
		if (parent.verticalAlignment.start == 0) {
			bottomY += yOffset / 2;
			topY -= yOffset / 2;
		}

		//Apply margins
		leftX += style.margin.left.getUnitizedValue(windowWidth);
		rightX -= style.margin.right.getUnitizedValue(windowWidth);

		//Move the element based on display type
		//TODO need to figure out display types needed

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

	public int getPosition() {
		return parent.elements.indexOf(this);
	}

	@Override
	public UIDimension getWidth() {
		return width;
	}

	@Override
	public UIDimension getHeight() {
		return height;
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
}
