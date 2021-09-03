package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.RectangleModel;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Margin;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIRenderable<T extends UIRenderable> {

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

	List<Consumer<T>> hoverConsumers;
	boolean lastHover;
	List<Consumer<T>> unHoverConsumers;
	List<Consumer<T>> leftClickConsumers;
	List<Consumer<T>> rightClickConsumers;
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
		for (Consumer<T> consumer : hoverConsumers) {
			consumer.accept((T)this);
		}
		lastHover = true;
	}

	public void callUnHover() {
		for (Consumer<T> consumer : unHoverConsumers) {
			consumer.accept((T)this);
		}
		lastHover = false;
	}

	public void callClick() {
		for (Consumer<T> consumer : leftClickConsumers) {
			consumer.accept((T)this);
		}
	}

	public void callRightClick() {
		for (Consumer<T> consumer : rightClickConsumers) {
			consumer.accept((T)this);
		}
	}

	public void callDoubleCLick(int time) {
		for (DoubleClickRunnable consumer : doubleClickConsumers) {
			if (consumer.tickTime >= time && time > 0) {
				consumer.consumer.accept(this);
			}
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

	public void resetBorderColor() {
		this.borderRed.unsetTarget();
		this.borderGreen.unsetTarget();
		this.borderBlue.unsetTarget();
		this.borderAlpha.unsetTarget();
	}

	public int getBorderRadius() {
		return (int)borderRadius.getValue();
	}

	public int getBorderThickness() {
		return (int)borderThickness.getValue();
	}

	public Margin getMargin() {
		return margin;
	}
}
