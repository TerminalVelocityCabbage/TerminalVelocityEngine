package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

public abstract class UIElement {

	public boolean built;

	public boolean itemsLeftAligned;
	public boolean itemsTopStart;

	public float marginLeft;
	public float marginRight;
	public float marginTop;
	public float marginBottom;

	public float width;
	public float height;

	public Vector3f color;
	public float backgroundOpacity;

	public float borderRadius;
	public Vector3f borderColor;
	public float borderWidth;

	public boolean isShown;

	public Vector3f position;
	public Mesh mesh;
	public UIElement parent;
	ArrayList<UIConstraint> constraints;
	ArrayList<UIElement> children;

	public UIElement() {
		built = false;
		itemsLeftAligned = true;
		itemsTopStart = true;
		marginLeft = 0f;
		marginRight = 0f;
		marginTop = 0f;
		marginBottom = 0f;
		color = new Vector3f(0);
		backgroundOpacity = 1f;
		borderRadius = 0;
		borderColor = new Vector3f(0);
		borderWidth = 0;
		isShown = false;
		position = new Vector3f(0);
		constraints = new ArrayList<>();
		children = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends UIElement>T build(UIElement parent) {
		this.parent = parent;
		for (UIConstraint constraint : constraints) {
			constraint.apply();
		}
		mesh = new Rectangle(
				new Vertex().setXYZ(-1f + marginLeft, 1f - marginTop, 0f),
				new Vertex().setXYZ(-1f + marginLeft, -1f + marginBottom, 0f),
				new Vertex().setXYZ(1f - marginRight, -1f + marginBottom, 0f),
				new Vertex().setXYZ(1f - marginRight, 1f- marginTop, 0f));
		mesh.bind();
		built = true;
		for (UIElement child : children) {
			child.build(this);
		}
		Log.info("build");
		return (T)this;
	}

	public void render() {
		render(0);
	}

	public void render(int order) {
		if (isShown) {
			position.setComponent(2, order);
			mesh.render();
		}
		Log.warn(order);
		Log.info(marginLeft + " " + marginRight + " " + marginTop + " " + marginBottom + " " + color);
		for (UIElement element : children) {
			element.render(order + 1);
		}
	}

	public void update() {
		if (isShown) {
			mesh.update(new Matrix4f());
			mesh.vertices[0].setXYZ(-1f + marginLeft, 1f - marginTop, 0f);
			mesh.vertices[1].setXYZ(-1f + marginLeft, -1f + marginBottom, 0f);
			mesh.vertices[2].setXYZ(1f - marginRight, -1f + marginBottom, 0f);
			mesh.vertices[3].setXYZ(1f - marginRight, 1f- marginTop, 0f);
			Log.error("Implement me! update");
		}
		for (UIElement element : children) {
			element.update();
		}
	}

	public void show() {
		Log.info("show");
		if (built) {
			isShown = true;
			for (UIElement element : children) {
				element.show();
			}
		} else {
			throw new RuntimeException("Cannot show an unbuilt UIElement");
		}
	}

	public void hide() {
		isShown = false;
		for (UIElement element : children) {
			element.hide();
		}
	}

	public UIElement getCanvas() {
		return parent.getCanvas();
	}

	//This is private on purpose as to not allow creation of elements outside the addChild method.
	private void setParent(UIElement element) {
		this.parent = element;
	}

	@SuppressWarnings("unchecked")
	public <T extends UIElement>T addConstraint(UIConstraint constraint) {
		constraint.setParent(this);
		constraints.add(constraint);
		return (T)this;
	}

	@SuppressWarnings("unchecked")
	public <T extends UIElement>T addChild(UIElement element) {
		element.setParent(this);
		children.add(element);
		return (T)this;
	}

	public void destroy() {
		Log.error("implement me! destroy");
	}
}
