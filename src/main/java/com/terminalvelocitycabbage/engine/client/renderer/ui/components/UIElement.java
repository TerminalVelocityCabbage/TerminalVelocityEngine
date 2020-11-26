package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;

public abstract class UIElement {

	public boolean built;
	public boolean isRoot;

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

	public UIElement parent;
	ArrayList<UIConstraint> constraints;
	ArrayList<UIElement> children;
	Model model;

	public UIElement() {
		built = false;
		isRoot = false;
		itemsLeftAligned = true;
		itemsTopStart = true;
		marginLeft = 0f;
		marginRight = 0f;
		marginTop = 0f;
		marginBottom = 0f;
		color = new Vector3f(0);
		backgroundOpacity = 0f;
		borderRadius = 0;
		borderColor = new Vector3f(0);
		borderWidth = 0;
		isShown = false;
		constraints = new ArrayList<>();
		children = new ArrayList<>();
	}

	@SuppressWarnings("unchecked")
	public <T extends UIElement>T build(UIElement parent) {
		this.parent = parent;
		for (UIElement child : children) {
			child.build(this);
		}
		for (UIConstraint constraint : constraints) {
			constraint.apply();
		}
		//TODO don't use models here at all, add a low level mesh draw API the reason I can't do it right now is the material system is only read through the model will need to implement this in the gui shader
		model = new Model(new ArrayList<>(Collections.singletonList(
				new Model.Part(
						new TexturedRectangle(
								new Vertex().setXYZ(marginLeft, marginTop, 1f),
								new Vertex().setXYZ(marginLeft, 1f - marginBottom, 1f),
								new Vertex().setXYZ(1f - marginRight, 1f - marginBottom, 1f),
								new Vertex().setXYZ(1f - marginRight, marginTop, 1f)),
						new Vector3f(0),
						new Vector3f(0),
						new Vector3f(0),
						new Vector3f(1),
						new ArrayList<>()
				))));
		model.setMaterial(Material.builder().color(color.x, color.y, color.z, backgroundOpacity).build());
		model.bind();
		built = true;
		Log.info("build");
		return (T)this;
	}

	public void render() {
		if (isShown) {
			model.render();
		}
		for (UIElement element : children) {
			element.render();
		}
	}

	public void update() {
		//do something
		for (UIElement element : children) {
			element.update();
		}
		Log.error("Implement me!");
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
		UIElement element = this;
		do {
			element = element.parent;
		} while (!element.isRoot);
		return element;
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
		Log.error("implement me!");
	}
}
