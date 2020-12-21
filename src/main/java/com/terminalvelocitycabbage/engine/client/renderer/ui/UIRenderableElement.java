package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public abstract class UIRenderableElement {

	boolean needsUpdate;
	Rectangle rectangle;
	public Style style;
	int zIndex;
	Matrix4f translationMatrix;

	List<Runnable> hoverConsumers;
	List<Runnable> leftClickConsumers;
	List<Runnable> rightClickConsumers;
	List<DoubleClickRunnable> doubleClickConsumers;

	public UIRenderableElement(Style style) {
		this.needsUpdate = false;
		this.rectangle = new Rectangle(new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0));
		this.style = style;
		this.zIndex = 0;
		this.translationMatrix = new Matrix4f();
		hoverConsumers = new ArrayList<>();
		leftClickConsumers = new ArrayList<>();
		rightClickConsumers = new ArrayList<>();
		doubleClickConsumers = new ArrayList<>();
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
		for (Runnable runnable : hoverConsumers) {
			runnable.run();
		}
	}

	public UIRenderableElement onHover(Runnable runnable) {
		hoverConsumers.add(runnable);
		return this;
	}

	public void callClick() {
		for (Runnable runnable : leftClickConsumers) {
			runnable.run();
		}
	}

	public UIRenderableElement onClick(Runnable runnable) {
		leftClickConsumers.add(runnable);
		return this;
	}

	public void callRightClick() {
		for (Runnable runnable : rightClickConsumers) {
			runnable.run();
		}
	}

	public UIRenderableElement onRightClick(Runnable runnable) {
		rightClickConsumers.add(runnable);
		return this;
	}

	public void callDoubleCLick(short time) {
		for (DoubleClickRunnable runnable : doubleClickConsumers) {
			if (runnable.tickTime >= time && time > 0) {
				runnable.runnable.run();
			}
		}
	}

	public UIRenderableElement onDoubleClick(short tickTime, Runnable runnable) {
		doubleClickConsumers.add(new DoubleClickRunnable(tickTime, runnable));
		return this;
	}

	private static class DoubleClickRunnable {

		short tickTime;
		Runnable runnable;

		public DoubleClickRunnable(short tickTime, Runnable runnable) {
			this.tickTime = tickTime;
			this.runnable = runnable;
		}
	}

	public abstract UIDimension getWidth();

	public abstract UIDimension getHeight();
}
