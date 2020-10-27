package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModeledGameObject extends EmptyGameObject {

	Model model;

	private ModeledGameObject(Vector3f position, Vector3f rotation, Vector3f scale, Model model) {
		super(position, rotation, scale);
		this.model = model;
		transformationMatrix = new Matrix4f();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	@Override
	public void update() {
		super.update();
		model.update(position, rotation, scale);
	}

	public void destroy() {
		model.destroy();
	}

	public static ModeledGameObject.Builder builder() {
		return new ModeledGameObject.Builder();
	}

	public static class Builder extends EmptyGameObject.Builder {
		Model model = null;

		public ModeledGameObject.Builder setModel(Model model) {
			this.model = model;
			return this;
		}

		@Override
		public ModeledGameObject build() {
			if (model == null) {
				throw new RuntimeException("A ModeledGameObject must have a model");
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
			return new ModeledGameObject(position, rotation, scale, model);
		}
	}
}
