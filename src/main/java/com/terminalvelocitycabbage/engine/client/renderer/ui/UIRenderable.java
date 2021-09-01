package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.RectangleModel;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Margin;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIRenderable {

	boolean needsUpdate;
	RectangleModel rectangle;

	AnimatableUIValue backgroundRed;
	AnimatableUIValue backgroundGreen;
	AnimatableUIValue backgroundBlue;
	AnimatableUIValue backgroundAlpha;
	AnimatableUIValue borderRed;
	AnimatableUIValue borderGreen;
	AnimatableUIValue borderBlue;
	AnimatableUIValue borderAlpha;
	AnimatableUIValue borderRadius;
	AnimatableUIValue borderThickness;
	Margin margin;

	List<Consumer<UIRenderable>> hoverConsumers;
	boolean lastHover;
	List<Consumer<UIRenderable>> unHoverConsumers;
	List<Consumer<UIRenderable>> leftClickConsumers;
	List<Consumer<UIRenderable>> rightClickConsumers;
	List<DoubleClickRunnable> doubleClickConsumers;

	public UIRenderable() {
		this.needsUpdate = false;
		this.rectangle = new RectangleModel(RenderFormat.POSITION,
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0),
			Vertex.position(0, 0, 0)
		);
		hoverConsumers = new ArrayList<>();
		lastHover = false;
		unHoverConsumers = new ArrayList<>();
		leftClickConsumers = new ArrayList<>();
		rightClickConsumers = new ArrayList<>();
		doubleClickConsumers = new ArrayList<>();

		backgroundRed = new AnimatableUIValue(1);
		backgroundGreen = new AnimatableUIValue(1);
		backgroundBlue = new AnimatableUIValue(1);
		backgroundAlpha = new AnimatableUIValue(1);

		borderRed = new AnimatableUIValue(0);
		borderGreen = new AnimatableUIValue(0);
		borderBlue = new AnimatableUIValue(0);
		borderAlpha = new AnimatableUIValue(0);

		borderRadius = new AnimatableUIValue(0);
		borderThickness = new AnimatableUIValue(0);

		margin = new Margin();
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

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public float getBackgroundRed() {
		return backgroundRed.getValue();
	}

	public float getBackgroundGreen() {
		return backgroundGreen.getValue();
	}

	public float getBackgroundBlue() {
		return backgroundBlue.getValue();
	}

	public float getBackgroundAlpha() {
		return backgroundAlpha.getValue();
	}

	public Vector4f getColor() {
		return new Vector4f(getBackgroundRed(), getBackgroundGreen(), getBackgroundBlue(), getBackgroundAlpha());
	}

	public UIRenderable color(float r, float g, float b, float a) {
		this.backgroundRed.setTarget(r);
		this.backgroundGreen.setTarget(g);
		this.backgroundBlue.setTarget(b);
		this.backgroundAlpha.setTarget(a);
		return this;
	}

	public void resetColor() {
		this.backgroundRed.unsetTarget();
		this.backgroundGreen.unsetTarget();
		this.backgroundBlue.unsetTarget();
		this.backgroundAlpha.unsetTarget();
	}

	public float getBorderRed() {
		return borderRed.getValue();
	}

	public float getBorderGreen() {
		return borderGreen.getValue();
	}

	public float getBorderBlue() {
		return borderBlue.getValue();
	}

	public float getBorderAlpha() {
		return borderAlpha.getValue();
	}

	public Vector4f getBorderColor() {
		return new Vector4f(getBorderRed(), getBorderGreen(), getBorderBlue(), getBorderAlpha());
	}

	public UIRenderable borderColor(float r, float g, float b, float a) {
		this.borderRed.setTarget(r);
		this.borderGreen.setTarget(g);
		this.borderBlue.setTarget(b);
		this.borderAlpha.setTarget(a);
		return this;
	}

	public void resetBorderColor() {
		this.borderRed.unsetTarget();
		this.borderGreen.unsetTarget();
		this.borderBlue.unsetTarget();
		this.borderAlpha.unsetTarget();
	}

	public int getBorderRadius() {
		return (int)borderRadius.getValue();
	}

	public UIRenderable borderRadius(int radius) {
		this.borderRadius.setTarget(radius);
		return this;
	}

	public int getBorderThickness() {
		return (int)borderThickness.getValue();
	}

	public UIRenderable borderThickness(int thickness) {
		this.borderThickness.setTarget(thickness);
		return this;
	}

	public UIRenderable margin(AnimatableUIValue value, UIDimension.Unit unit) {
		return margins(value, value, value, value).marginUnits(unit, unit, unit, unit);
	}

	public UIRenderable margins(AnimatableUIValue left, AnimatableUIValue right, AnimatableUIValue top, AnimatableUIValue bottom) {
		this.margin.setMargins(left, right, top, bottom);
		return this;
	}

	public UIRenderable marginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
		this.margin.setMarginUnits(left, right, top, bottom);
		return this;
	}

	public UIRenderable marginLeft(AnimatableUIValue value, UIDimension.Unit unit) {
		this.margin.setLeft(value, unit);
		return this;
	}

	public UIRenderable marginRight(AnimatableUIValue value, UIDimension.Unit unit) {
		this.margin.setRight(value, unit);
		return this;
	}

	public UIRenderable marginTop(AnimatableUIValue value, UIDimension.Unit unit) {
		this.margin.setTop(value, unit);
		return this;
	}

	public UIRenderable marginBottom(AnimatableUIValue value, UIDimension.Unit unit) {
		this.margin.setBottom(value, unit);
		return this;
	}

	public Margin getMargin() {
		return margin;
	}
}
