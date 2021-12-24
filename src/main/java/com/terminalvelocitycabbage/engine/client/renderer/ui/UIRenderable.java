package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.RectangleModel;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Margin;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIRenderable<T extends UIRenderable> {

	boolean needsUpdate;

	final Vertex vertex1;
	final Vertex vertex2;
	final Vertex vertex3;
	final Vertex vertex4;

	final Vertex[] vertices;

	public final Model.Part part;


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

		this.vertex1 = Vertex.ui(0, 0, 0, -1, -1, 1, 1, 1, 1, 0, 0);
		this.vertex2 = Vertex.ui(0, 0, 0, -1, 1, 1, 1, 1, 1, 0, 0);
		this.vertex3 = Vertex.ui(0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0);
		this.vertex4 = Vertex.ui(0, 0, 0, 1, -1, 1, 1, 1, 1, 0, 0);

		this.vertices = new Vertex[] {
			this.vertex1, this.vertex2, this.vertex3, this.vertex4
		};

		this.part = new Model.Part(
			new MeshPart(this.vertices, new int[] { 0, 1, 2, 2, 3, 0 })
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

	public abstract void update();

	public boolean isRoot() {
		return false;
	}

	public void queueUpdate() {
		this.needsUpdate = true;
	}

	public abstract void onPartsChange();

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
			if (consumer.shouldAccept(time)) {
				consumer.consumer.accept(this);
			}
		}
	}

	public float getStartX() {
		return this.vertex1.getX();
	}

	public float getWidth() {
		return this.vertex4.getX() - this.vertex1.getX();
	}

	public float getStartY() {
		return this.vertex2.getY();
	}

	public float getHeight() {
		return this.vertex1.getY() - this.vertex2.getY();
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public float getBgRed() {
		return backgroundRed.getValue();
	}

	public float getBgGreen() {
		return backgroundGreen.getValue();
	}

	public float getBgBlue() {
		return backgroundBlue.getValue();
	}

	public float getBgAlpha() {
		return backgroundAlpha.getValue();
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

	public void resetBorderColor() {
		this.borderRed.unsetTarget();
		this.borderGreen.unsetTarget();
		this.borderBlue.unsetTarget();
		this.borderAlpha.unsetTarget();
	}

	public float getBorderRadius() {
		return borderRadius.getValue();
	}

	public int getBorderThickness() {
		return (int)borderThickness.getValue();
	}

	public Margin getMargin() {
		return margin;
	}
}
