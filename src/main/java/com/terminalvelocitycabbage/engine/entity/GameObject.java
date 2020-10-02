package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import org.joml.Vector3f;

public class GameObject {

	Vector3f position;
	Vector3f rotation;
	Vector3f scale;

	Model model;

	private GameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.model = model;
	}

	public static GameObject.Builder builder() {
		return new GameObject.Builder();
	}

	public static class Builder {
		Vector3f position = null;
		Vector3f rotation = null;
		Vector3f scale = null;
		Model model = null;

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
				scale = new Vector3f(0, 0, 0); //TODO make sure this isn't supposed to be 1, 1, 1
			}
			return new GameObject(position, rotation, scale, model);
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

}
