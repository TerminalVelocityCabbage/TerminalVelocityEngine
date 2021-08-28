package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.RectangleModel;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIRenderable {

	boolean needsUpdate;
	RectangleModel rectangle;
	public Style style;

	List<Consumer<UIRenderable>> hoverConsumers;
	boolean lastHover;
	List<Consumer<UIRenderable>> unHoverConsumers;
	List<Consumer<UIRenderable>> leftClickConsumers;
	List<Consumer<UIRenderable>> rightClickConsumers;
	List<DoubleClickRunnable> doubleClickConsumers;

	public UIRenderable(Style style) {
		this.needsUpdate = false;
		this.rectangle = new RectangleModel(RenderFormat.POSITION,
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0)
		);
		this.style = style;
		hoverConsumers = new ArrayList<>();
		lastHover = false;
		unHoverConsumers = new ArrayList<>();
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

	public abstract void renderText();

	public void destroy() {
		rectangle.destroy();
	}

	public void queueUpdate() {
		this.needsUpdate = true;
	}

	public Model getRectangle() {
		return rectangle;
	}

	public void callHoverable() {
		for (Consumer<UIRenderable> consumer : hoverConsumers) {
			consumer.accept(this);
		}
		lastHover = true;
	}

	public UIRenderable onHover(Consumer<UIRenderable> consumer) {
		hoverConsumers.add(consumer);
		return this;
	}

	public void callUnHover() {
		for (Consumer<UIRenderable> consumer : unHoverConsumers) {
			consumer.accept(this);
		}
		lastHover = false;
	}

	public UIRenderable onUnHover(Consumer<UIRenderable> consumer) {
		unHoverConsumers.add(consumer);
		return this;
	}

	public void callClick() {
		for (Consumer<UIRenderable> consumer : leftClickConsumers) {
			consumer.accept(this);
		}
	}

	public UIRenderable onClick(Consumer<UIRenderable> consumer) {
		leftClickConsumers.add(consumer);
		return this;
	}

	public void callRightClick() {
		for (Consumer<UIRenderable> consumer : rightClickConsumers) {
			consumer.accept(this);
		}
	}

	public UIRenderable onRightClick(Consumer<UIRenderable> consumer) {
		rightClickConsumers.add(consumer);
		return this;
	}

	public void callDoubleCLick(int time) {
		for (DoubleClickRunnable consumer : doubleClickConsumers) {
			if (consumer.tickTime >= time && time > 0) {
				consumer.consumer.accept(this);
			}
		}
	}

	public UIRenderable onDoubleClick(int tickTime, Consumer<UIRenderable> consumer) {
		doubleClickConsumers.add(new DoubleClickRunnable(tickTime, consumer));
		return this;
	}

	private static class DoubleClickRunnable {

		int tickTime;
		Consumer<UIRenderable> consumer;

		public DoubleClickRunnable(int tickTime, Consumer<UIRenderable> consumer) {
			this.tickTime = tickTime;
			this.consumer = consumer;
		}
	}

	public float getWidth() {
		return rectangle.vertices[3].getX() - rectangle.vertices[0].getX();
	}

	public float getHeight() {
		return rectangle.vertices[0].getY() - rectangle.vertices[1].getY();
	}
}
