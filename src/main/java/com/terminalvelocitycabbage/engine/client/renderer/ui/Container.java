package com.terminalvelocitycabbage.engine.client.renderer.ui;

import java.util.ArrayList;
import java.util.List;

public class Container extends UIRenderableElement {

	public UIDimension width;
	public UIDimension height;
	public Anchor anchorPoint;
	public Alignment.Horizontal horizontalAlignment;
	public Alignment.Vertical verticalAlignment;
	public UIRenderableElement parent;

	public List<Container> childContainers;
	public List<Element> elements;

	public Container(UIDimension width, UIDimension height, Anchor anchorPoint, Style style) {
		super(style);
		this.width = width;
		this.height = height;
		this.anchorPoint = anchorPoint;
		this.horizontalAlignment = Alignment.Horizontal.LEFT;
		this.verticalAlignment = Alignment.Vertical.TOP;
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

	public void setParent(UIRenderableElement canvas) {
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

	public Style getStyle() {
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
			float uContainerWidth = (float)parent.getWidth().getPixelValue(windowWidth) / windowWidth;
			float uContainerHeight = (float)parent.getHeight().getPixelValue(windowHeight) / windowHeight;

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

			for (Container container : childContainers) {
				container.update();
			}
			for (Element element : elements) {
				element.update();
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
		return this;
	}

	public Container addElement(Element element) {
		element.setParent(this);
		element.zIndex = zIndex - 1;
		elements.add(element);
		element.bind();
		return this;
	}

	public float getHeightOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			//TODO accommodate for eventual margins
			value += element.getHeight().getUnitizedValue(getCanvas().getWindow().height());
		}
		return value;
	}

	public float getWidthOfElements(int beginIndex, int endIndex) {
		float value = 0f;
		for (Element element : elements.subList(beginIndex, endIndex)) {
			//TODO accommodate for eventual margins
			value += element.getWidth().getUnitizedValue(getCanvas().getWindow().width());
		}
		return value;
	}

	@Override
	public Container onHover(Runnable runnable) {
		return (Container) super.onHover(runnable);
	}

	@Override
	public Container onClick(Runnable runnable) {
		return (Container) super.onClick(runnable);
	}

	@Override
	public Container onRightClick(Runnable runnable) {
		return (Container) super.onRightClick(runnable);
	}

	@Override
	public Container onDoubleClick(short tickTime, Runnable runnable) {
		return (Container) super.onDoubleClick(tickTime, runnable);
	}

	@Override
	public UIDimension getWidth() {
		return width;
	}

	@Override
	public UIDimension getHeight() {
		return height;
	}

	@Override
	public void render() {
		super.render();
		childContainers.forEach(Container::render);
		elements.forEach(UIRenderableElement::render);
	}
}
