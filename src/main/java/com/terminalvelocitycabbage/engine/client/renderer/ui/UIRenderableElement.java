package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public abstract class UIRenderableElement {

	boolean needsUpdate;
	Rectangle rectangle;
	public Style style;
	int zIndex;
	int width;
	int height;
	Matrix4f translationMatrix;

	List<Runnable> hoverConsumers;

	public UIRenderableElement(Style style) {
		this.needsUpdate = false;
		this.rectangle = new Rectangle(new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0));
		this.style = style;
		this.zIndex = 0;
		this.width = 0;
		this.height = 0;
		this.translationMatrix = new Matrix4f();
		hoverConsumers = new ArrayList<>();
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

	public void callHoverable() {
		Log.error("Call Hoverable");
		for (Runnable runnable : hoverConsumers) {
			runnable.run();
		}
	}

	public UIRenderableElement onHover(Runnable runnable) {
		hoverConsumers.add(runnable);
		return this;
	}
}
