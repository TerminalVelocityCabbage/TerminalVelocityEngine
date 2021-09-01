package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;

public class Container extends UIRenderable<Container> {

	public UIDimension width;
	public UIDimension height;
	public Anchor anchorPoint;
	public Alignment.Horizontal horizontalAlignment;
	public Alignment.Vertical verticalAlignment;
	public Alignment.Direction alignmentDirection;
	public UIRenderable parent;
	public Overflow overflow;
	public Wrap wrap;

	public List<Container> childContainers;
	public List<Element> elements;

	public Container(UIDimension width, UIDimension height, Anchor anchorPoint) {
		this.width = width;
		this.height = height;
		this.anchorPoint = anchorPoint;
		this.horizontalAlignment = Alignment.Horizontal.LEFT;
		this.verticalAlignment = Alignment.Vertical.TOP;
		this.alignmentDirection = Alignment.Direction.HORIZONTAL;
		this.overflow = Overflow.SHOWN;
		this.wrap = Wrap.WRAP;
		this.childContainers = new ArrayList<>();
		this.elements = new ArrayList<>();
		this.backgroundAlpha = new AnimatableUIValue(0);
	}

	public Canvas getCanvas() {
		if (parent.isRoot()) {
			return (Canvas)parent;
		} else {
			return ((Container)parent).getCanvas();
		}
	}

	public void setParent(UIRenderable canvas) {
		this.parent = canvas;
	}

	public Container horizontalAlignment(Alignment.Horizontal alignment) {
		this.horizontalAlignment = alignment;
		return this;
	}

	public Container verticalAlignment(Alignment.Vertical alignment) {
		this.verticalAlignment = alignment;
		return this;
	}

	public Container alignmentDirection(Alignment.Direction alignment) {
		this.alignmentDirection = alignment;
		return this;
	}

	@Override
	public void update() {

		if (needsUpdate) {

			//Window dimensions
			int windowWidth = getCanvas().getWindow().width();
			int windowHeight = getCanvas().getWindow().height();

			//Screen dimensions
			int screenWidth = getCanvas().getWindow().monitorWidth();
			int screenHeight = getCanvas().getWindow().monitorHeight();

			//Get boundaries of parent
			float originXMin = parent.rectangle.vertices[0].getX() + ((float)parent.getBorderThickness() / windowWidth * 2);
			float originYMin = parent.rectangle.vertices[1].getY() + ((float)parent.getBorderThickness() / windowHeight * 2);
			float originXMax = parent.rectangle.vertices[2].getX() - ((float)parent.getBorderThickness() / windowWidth * 2);
			float originYMax = parent.rectangle.vertices[0].getY() - ((float)parent.getBorderThickness() / windowHeight * 2);

			//Get parent's unit dimensions
			float uContainerWidth = originXMax - originXMin;
			float uContainerHeight = originYMax - originYMin;

			//Container center
			float containerCenterX = (originXMin + originXMax) / 2;
			float containerCenterY = (originYMin + originYMax) / 2;

			//Store offsets for anchorPosition center
			float xOffset = anchorPoint.getAnchorPoint().xPos * (uContainerWidth / 2);
			float yOffset = anchorPoint.getAnchorPoint().yPos * (uContainerHeight / 2);

			//Unit dimensions
			//Create temp width and height vars in case of a responsive layout
			float uWidth = width.getUnitDirect().equals(PERCENT) ? width.getValueDirect() / 100f * uContainerWidth : width.getUnitizedValue(screenWidth, windowWidth);
			float uHeight = height.getUnitDirect().equals(PERCENT) ? height.getValueDirect() / 100f * uContainerHeight : height.getUnitizedValue(screenHeight, windowHeight);

			//Place all vertices at the center of the parent
			float leftX = containerCenterX;
			float rightX = containerCenterX;
			float topY = containerCenterY;
			float bottomY = containerCenterY;

			//Store offsets for anchor direction
			float xDirOffset = anchorPoint.getAnchorDirection().xDirection * (uWidth / 2);
			float yDirOffset = anchorPoint.getAnchorDirection().yDirection * (uHeight / 2);

			//Give the container dimensions
			leftX -= uWidth / 2;
			rightX += uWidth / 2;
			bottomY -= uHeight / 2;
			topY += uHeight / 2;

			//Apply margins
			leftX += getMargin().left().getUnitizedValue(screenWidth, windowWidth);
			rightX -= getMargin().right().getUnitizedValue(screenWidth, windowWidth);
			bottomY += getMargin().bottom().getUnitizedValue(screenHeight, windowHeight);
			topY -= getMargin().top().getUnitizedValue(screenHeight, windowHeight);

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
			rectangle.vertices[0].setXYZ(leftX, topY, 0);
			rectangle.vertices[1].setXYZ(leftX, bottomY, 0);
			rectangle.vertices[2].setXYZ(rightX, bottomY, 0);
			rectangle.vertices[3].setXYZ(rightX, topY, 0);

			//Update the data that gets passed to the gpu
			rectangle.update(new Vector3f(), new Quaternionf().identity(), new Vector3f(1F)); //TODO

			for (Container container : childContainers) {
				container.queueUpdate();
				container.update();
			}
			for (Element element : elements) {
				element.queueUpdate();
				element.update();
			}

			//Once we've updated all elements we need to center them if requested by the user
			if (horizontalAlignment.equals(Alignment.Horizontal.CENTER)) {
				//These temp vars save info about rows needed to update in index range
				int tmpRowIndex = 0;
				float tmpRowPos = elements.get(0).rectangle.vertices[0].getY();
				for (int i = 0; i < elements.size(); i++) {
					//This element is on the next row, so move them based on remainder space and update temp vals
					if (elements.get(i).rectangle.vertices[3].getY() < tmpRowPos) {
						moveElementsHorizontal(tmpRowIndex, i, rectangle.vertices[2].getX() - elements.get(i - 1).rectangle.vertices[2].getX());
						tmpRowIndex = i;
						tmpRowPos = elements.get(i).rectangle.vertices[3].getY();
					}
				}
				//Update the last row
				moveElementsHorizontal(tmpRowIndex, elements.size(), (rectangle.vertices[2].getX() - elements.get(elements.size() - 1).rectangle.vertices[2].getX()));
			}
			if (verticalAlignment.equals(Alignment.Vertical.CENTER)) {
				//These temp vars save info about columns needed to update in index range
				int tmpColIndex = 0;
				float tmpColPos = elements.get(0).rectangle.vertices[0].getX();
				for (int i = 0; i < elements.size(); i++) {
					//This element is on the next column, so move them based on remainder space and update temp vals
					if (elements.get(i).rectangle.vertices[3].getX() < tmpColPos) {
						moveElementsVertical(tmpColIndex, i, rectangle.vertices[2].getY() - elements.get(i - 1).rectangle.vertices[2].getY());
						tmpColIndex = i;
						tmpColPos = elements.get(i).rectangle.vertices[3].getX();
					}
				}
				//Update the last row
				moveElementsVertical(tmpColIndex, elements.size(), (rectangle.vertices[2].getY() - elements.get(elements.size() - 1).rectangle.vertices[2].getY()));
			}

			//Make sure text is updated with all these elements
			for (Element element : elements) {
				if (element.innerText != null) {
					element.innerText.update(element.width.getPixelValue(this.getCanvas().getWindow().width()), this.getCanvas().getWindow(), element.rectangle.vertices[0].getX(), element.rectangle.vertices[0].getY());
				}
			}

			//Complete this update
			needsUpdate = false;
		}
	}

	/**
	 * @param beginIndex start index of the row being moved
	 * @param endIndex end index of row being moved
	 * @param distance distance between end of row element edge and edge of container
	 */
	private void moveElementsHorizontal(int beginIndex, int endIndex, float distance) {
		distance /= 2f;
		distance -= (float)getBorderThickness() / (float)getCanvas().window.monitorWidth();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			element.rectangle.vertices[0].addXYZ(distance, 0, 0);
			element.rectangle.vertices[1].addXYZ(distance, 0, 0);
			element.rectangle.vertices[2].addXYZ(distance, 0, 0);
			element.rectangle.vertices[3].addXYZ(distance, 0, 0);
		}
	}

	/**
	 * @param beginIndex start index of the row being moved
	 * @param endIndex end index of row being moved
	 * @param distance distance between end of row element edge and edge of container
	 */
	private void moveElementsVertical(int beginIndex, int endIndex, float distance) {
		distance /= 2f;
		distance -= (float)getBorderThickness() / (float)getCanvas().window.monitorHeight();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			element.rectangle.vertices[0].addXYZ(0, distance, 0);
			element.rectangle.vertices[1].addXYZ(0, distance, 0);
			element.rectangle.vertices[2].addXYZ(0, distance, 0);
			element.rectangle.vertices[3].addXYZ(0, distance, 0);
		}
	}

	public Container addContainer(Container container) {
		container.setParent(this);
		container.bind();
		container.queueUpdate();
		childContainers.add(container);
		return this;
	}

	public Container addElement(Element element) {
		element.setParent(this);
		element.bind();
		element.queueUpdate();
		elements.add(element);
		return this;
	}

	public float getHeightOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value += element.getHeight();
		}
		return value;
	}

	public float getWidthOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value += element.getWidth();
		}
		return value;
	}

	public float getMinXOfElements(int beginIndex, int endIndex) {
		float value = rectangle.vertices[2].getX();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value = Math.min(value, element.rectangle.vertices[0].getX());
		}
		return value;
	}

	public float getMaxXOfElements(int beginIndex, int endIndex) {
		float value = rectangle.vertices[0].getX();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value = Math.max(value, element.rectangle.vertices[2].getX());
		}
		return value;
	}

	public float getMinYOfElements(int beginIndex, int endIndex) {
		float value = rectangle.vertices[0].getY();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value = Math.min(value, element.rectangle.vertices[1].getY());
		}
		return value;
	}

	public float getMaxYOfElements(int beginIndex, int endIndex) {
		float value = rectangle.vertices[2].getY();
		for (Element element : elements.subList(beginIndex, endIndex)) {
			value = Math.max(value, element.rectangle.vertices[0].getY());
		}
		return value;
	}

	public Container overflow(Overflow overflow) {
		this.overflow = overflow;
		return this;
	}

	public Container wrap(Wrap wrap) {
		this.wrap = wrap;
		return this;
	}

	public List<Container> getAllContainers() {
		List<Container> child = new ArrayList<>();
		child.addAll(childContainers);
		childContainers.forEach(container -> child.addAll(container.getAllContainers()));
		return child;
	}

	public List<Container> getChildContainers() {
		return childContainers;
	}

	public List<Element> getElements() {
		return elements;
	}

}
