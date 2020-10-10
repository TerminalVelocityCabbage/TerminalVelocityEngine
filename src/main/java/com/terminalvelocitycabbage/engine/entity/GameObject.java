package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GameObject {

	Vector3f position;
	Vector3f rotation;
	Vector3f scale;

	Model model;

	Matrix4f modelViewMatrix;

	boolean textured;

	private GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model, boolean textured) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
		this.modelViewMatrix = new Matrix4f();
		this.textured = textured;
	}

	private GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
		this.modelViewMatrix = new Matrix4f();

		this.textured = true;
	}

	public static GameObject.Builder builder() {
		return new GameObject.Builder();
	}

	public static class Builder {
		Vector3f position = null;
		Vector3f rotation = null;
		Vector3f scale = null;
		Model model = null;
		boolean textured;

		public GameObject.Builder setPosition(Vector3f position) {
			this.position = position;
			return this;
		}

		public GameObject.Builder setRotation(Vector3f rotation) {
			this.rotation = rotation;
			return this;
		}

		public GameObject.Builder setScale(Vector3f scale) {
			this.scale = scale;
			return this;
		}

		public GameObject.Builder setModel(Model model) {
			this.model = model;
			this.textured = true;
			return this;
		}

		public GameObject.Builder setModel(Model model, boolean textured) {
			this.model = model;
			this.textured = textured;
			return this;
		}

		public GameObject build() {
			if (model == null) {
				throw new RuntimeException("A GameObject must have a model");
			}
			if (position == null) {
				position = new Vector3f(0, 0, 0);
			}
			if (rotation == null) {
				rotation = new Vector3f(0, 0, 0);
			}
			if (scale == null) {
				scale = new Vector3f(1, 1, 1);
			}
			return new GameObject(position, rotation, scale, model, textured);
		}
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
		model.move(x, y, z);
		update();
	}

	public void rotate(float x, float y, float z) {
		rotation.add(x, y, z);
		model.rotate(x, y, z);
		update();
	}

	public void scale(float x, float y, float z) {
		scale.add(x, y, z);
		model.scale(x, y, z);
		update();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	public void update() {
		model.update();
	}

	public void destroy() {
		model.destroy();
	}

	public Matrix4f getModelViewMatrix(Matrix4f viewMatrix) {
		modelViewMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(modelViewMatrix);
	}

	public boolean isTextured() {
		return textured;
	}
}
