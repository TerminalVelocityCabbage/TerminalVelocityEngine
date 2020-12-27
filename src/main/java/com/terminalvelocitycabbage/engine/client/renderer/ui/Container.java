package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension.Unit.PERCENT;

public class Container extends UIRenderable {

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

	public Container(UIDimension width, UIDimension height, Anchor anchorPoint, Style style) {
		super(style);
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

	public Style getStyle() {
		return style;
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
			float originXMin = parent.rectangle.vertices[0].getX() + ((float)parent.style.getBorderThickness() / windowWidth * 2);
			float originYMin = parent.rectangle.vertices[1].getY() + ((float)parent.style.getBorderThickness() / windowHeight * 2);
			float originXMax = parent.rectangle.vertices[2].getX() - ((float)parent.style.getBorderThickness() / windowWidth * 2);
			float originYMax = parent.rectangle.vertices[0].getY() - ((float)parent.style.getBorderThickness() / windowHeight * 2);

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
			leftX += style.getMargin().left().getUnitizedValue(screenWidth, windowWidth);
			rightX -= style.getMargin().right().getUnitizedValue(screenWidth, windowWidth);
			bottomY += style.getMargin().bottom().getUnitizedValue(screenHeight, windowHeight);
			topY -= style.getMargin().top().getUnitizedValue(screenHeight, windowHeight);

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

			for (Container container : childContainers) {
				container.queueUpdate();
			}
			for (Element element : elements) {
				element.queueUpdate();
			}

			//Complete this update
			needsUpdate = false;
		}
	}

	public Container addContainer(Container container) {
		container.setParent(this);
		container.zIndex = zIndex - 1;
		childContainers.add(container);
		container.bind();
		container.queueUpdate();
		return this;
	}

	public Container addElement(Element element) {
		element.setParent(this);
		element.zIndex = zIndex - 1;
		elements.add(element);
		element.bind();
		element.queueUpdate();
		return this;
	}

	public float getHeightOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			//TODO accommodate for eventual margins
			value += element.getHeight();
		}
		return value;
	}

	public float getWidthOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			//TODO accommodate for eventual margins
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

	@Override
	public Container onHover(Consumer<UIRenderable> consumer) {
		return (Container) super.onHover(consumer);
	}

	@Override
	public Container onUnHover(Consumer<UIRenderable> consumer) {
		return (Container) super.onUnHover(consumer);
	}

	@Override
	public Container onClick(Consumer<UIRenderable> consumer) {
		return (Container) super.onClick(consumer);
	}

	@Override
	public Container onRightClick(Consumer<UIRenderable> consumer) {
		return (Container) super.onRightClick(consumer);
	}

	@Override
	public Container onDoubleClick(short tickTime, Consumer<UIRenderable> consumer) {
		return (Container) super.onDoubleClick(tickTime, consumer);
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
