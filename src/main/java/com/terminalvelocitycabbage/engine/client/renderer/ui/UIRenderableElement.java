package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import org.joml.Matrix4f;

public abstract class UIRenderableElement {

	boolean needsUpdate;
	Rectangle rectangle;
	public UIStyle style;
	int zIndex;
	int width;
	int height;
	Matrix4f translationMatrix;

	public UIRenderableElement(UIStyle style) {
		this.needsUpdate = false;
		this.rectangle = new Rectangle(new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0));
		this.style = style;
		this.zIndex = 0;
		this.width = 0;
		this.height = 0;
		this.translationMatrix = new Matrix4f();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void bind() {
		rectangle.bind();
	}

	public abstract void update();

	public boolean isRoot() {
		return false;
	}

	public void render() {
		rectangle.render();
	}

	public void destroy() {
		rectangle.destroy();
	}

	public void queueUpdate() {
		this.needsUpdate = true;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
}
