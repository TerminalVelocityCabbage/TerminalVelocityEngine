package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModeledGameObject extends EmptyGameObject {

	Model model;
	boolean textured;

	private ModeledGameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model, boolean textured) {
		super(position, rotation, scale);
		this.model = model;
		this.textured = textured;
		modelViewMatrix = new Matrix4f();
	}

	private ModeledGameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model) {
		super(position, rotation, scale);
		this.model = model;
		this.textured = true;
		modelViewMatrix = new Matrix4f();
	}

	@Override
	public void bind() {
		model.bind();
	}

	@Override
	public void render() {
		model.render();
	}

	@Override
	public void update() {
		super.update();
		model.update(position, rotation, scale);
	}

	@Override
	public void destroy() {
		model.destroy();
	}

	public boolean isTextured() {
		return textured;
	}

	public static ModeledGameObject.Builder builder() {
		return new ModeledGameObject.Builder();
	}

	public static class Builder extends EmptyGameObject.Builder {
		Model model = null;
		boolean textured;

		public ModeledGameObject.Builder setModel(Model model) {
			this.model = model;
			this.textured = true;
			return this;
		}

		public ModeledGameObject.Builder setModel(Model model, boolean textured) {
			this.model = model;
			this.textured = textured;
			return this;
		}

		@Override
		public ModeledGameObject build() {
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
			return new ModeledGameObject(position, rotation, scale, model, textured);
		}
	}
}
