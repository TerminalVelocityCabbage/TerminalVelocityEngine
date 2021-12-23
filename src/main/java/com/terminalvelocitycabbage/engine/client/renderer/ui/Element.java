package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.FontMeshPartStorage;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;

import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment.Horizontal.LEFT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment.Horizontal.RIGHT;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment.Vertical.BOTTOM;
import static com.terminalvelocitycabbage.engine.client.renderer.ui.components.Alignment.Vertical.TOP;

public class Element extends UIRenderable<Element> {

	public Container parent;

	public UIDimension width;
	public UIDimension height;
	public Text innerText;

	public Element(Container parent) {
		this.parent = parent;
	}



	@Override
	public void update() {

		if (needsUpdate()) {

			//Window dimensions
			int windowWidth = getCanvas().window.width();
			int windowHeight = getCanvas().window.height();

			//Screen dimensions (reference for px)
			int screenWidth = getCanvas().window.monitorWidth();
			int screenHeight = getCanvas().window.monitorHeight();

			//Position this element is in it's parent's element list
			int position = getPosition();

			//Get boundaries of parent
			float originXMin = parent.vertex1.getX() + ((float)parent.getBorderThickness() / windowWidth * 2);
			float originXMax = parent.vertex3.getX() - ((float)parent.getBorderThickness() / windowWidth * 2);
			float originYMin = parent.vertex2.getY() + ((float)parent.getBorderThickness() / windowHeight * 2);
			float originYMax = parent.vertex1.getY() - ((float)parent.getBorderThickness() / windowHeight * 2);

			//Create temp width and height vars in case of a responsive layout
			float width = this.width.getUnitizedValue(screenWidth, windowWidth);
			float height = this.height.getUnitizedValue(screenHeight, windowHeight);

			//Create temp vars for the parent's alignment
			Alignment.Horizontal horizontalAlignment = parent.horizontalAlignment;
			Alignment.Vertical verticalAlignment = parent.verticalAlignment;
			//Treat centered alignment as top or left, we will worry about centering them after this
			//update call in the container class since it is simpler to adjust all elements on a row
			//instead of making assumptions and adjusting previous elements multiple times
			if (horizontalAlignment.equals(Alignment.Horizontal.CENTER)) horizontalAlignment = LEFT;
			if (verticalAlignment.equals(Alignment.Vertical.CENTER)) verticalAlignment = TOP;

			//Get the start point of the element
			float startX = horizontalAlignment.equals(LEFT) ? originXMin : originXMax;
			float startY = verticalAlignment.equals(BOTTOM) ? originYMin : originYMax;
			//If there is an element before this on in the parent's element list then use a start point relative to that instead.
			if (position > 0) {
				if (parent.alignmentDirection.equals(Alignment.Direction.HORIZONTAL)) {
					if (horizontalAlignment.equals(LEFT)) startX = parent.elements.get(position - 1).vertex3.getX();
					if (horizontalAlignment.equals(RIGHT)) startX = parent.elements.get(position - 1).vertex1.getX();
					if (verticalAlignment.equals(TOP)) startY = parent.elements.get(position - 1).vertex1.getY();
					if (verticalAlignment.equals(BOTTOM)) startY = parent.elements.get(position - 1).vertex2.getY();
				}
				if (parent.alignmentDirection.equals(Alignment.Direction.VERTICAL)) {
					if (horizontalAlignment.equals(LEFT)) startX = parent.elements.get(position - 1).vertex1.getX();
					if (horizontalAlignment.equals(RIGHT)) startX = parent.elements.get(position - 1).vertex3.getX();
					if (verticalAlignment.equals(TOP)) startY = parent.elements.get(position - 1).vertex2.getY();
					if (verticalAlignment.equals(BOTTOM)) startY = parent.elements.get(position - 1).vertex1.getY();
				}
			}

			//if there is overflow move the element start as to not overflow, but only if the user wants it to wrap
			if (parent.wrap.isWrap()) {
				if (parent.alignmentDirection.equals(Alignment.Direction.HORIZONTAL)) {
					if (horizontalAlignment.equals(LEFT) && startX + width > originXMax) {
						startX = originXMin;
						startY = verticalAlignment.equals(TOP) ? parent.getMinYOfElements(0, position) : parent.getMaxYOfElements(0, position);
					}
					if (horizontalAlignment.equals(RIGHT) && startX - width < originXMin) {
						startX = originXMax;
						startY = verticalAlignment.equals(TOP) ? parent.getMinYOfElements(0, position) : parent.getMaxYOfElements(0, position);
					}
				}
				if (parent.alignmentDirection.equals(Alignment.Direction.VERTICAL)) {
					if (verticalAlignment.equals(BOTTOM) && startY + height > originYMax) {
						startY = originYMin;
						startX = horizontalAlignment.equals(RIGHT) ? parent.getMinXOfElements(0, position) : parent.getMaxXOfElements(0, position);
					}
					if (verticalAlignment.equals(TOP) && startY - height < originYMin) {
						startY = originYMax;
						startX = horizontalAlignment.equals(RIGHT) ? parent.getMinXOfElements(0, position) : parent.getMaxXOfElements(0, position);
					}
				}
			}

			//Create the element position vars
			float leftX = startX;
			float rightX = startX;
			float topY = startY;
			float bottomY = startY;

			//Give the element it's dimensions
			leftX -= width / 2f;
			rightX += width / 2f;
			bottomY -= height / 2f;
			topY += height / 2f;

			//Offset them in the opposite of the start to make them position with their corner/edge in the right spot
			leftX += (width / 2) * -horizontalAlignment.getStart();
			rightX += (width / 2) * -horizontalAlignment.getStart();
			topY += (height / 2) * -verticalAlignment.getStart();
			bottomY += (height / 2) * -verticalAlignment.getStart();

			//Hide overflow if requested
			if (parent.overflow.hideX()) {
				if (leftX < originXMin) leftX = originXMin;
				if (rightX > originXMax) rightX = originXMax;
			}
			if (parent.overflow.hideY()) {
				if (bottomY < originYMin) bottomY = originYMin;
				if (topY > originYMax) topY = originYMax;
			}

			//Set the vertexes based on the calculated positions
			vertex1.setXYZ(leftX, topY, 0);
			vertex2.setXYZ(leftX, bottomY, 0);
			vertex3.setXYZ(rightX, bottomY, 0);
			vertex4.setXYZ(rightX, topY, 0);
		}

		//Pass the update to the text and let it determine if it's required
		if (this.innerText != null) {
			this.innerText.update(this.width.getPixelValue(this.getCanvas().getWindow().width()), this.getCanvas().getWindow(), this.vertex1.getX(), this.vertex1.getY());
		}

		//Complete this update
		queueUpdate();
	}

	@Override
	public void onPartsChange() {
		this.parent.onPartsChange();
	}

	public boolean hasText() {
		return innerText != null && !innerText.getString().equals("");
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

	public Element setInnerText(Text text) {
		this.innerText = text;
		this.innerText.bind();
		return this;
	}

	public Element updateTextString(String text) {
		this.innerText.setTextString(text);
		return this;
	}

	public Element changeFont(FontMeshPartStorage font) {
		this.innerText.setFont(font);
		return this;
	}

	public Text getInnerText() {
		return innerText;
	}

	public void renderText() {
		if (this.innerText != null) {
			this.innerText.render();
		}
	}

	public Text getText() {
		return this.innerText;
	}
}
