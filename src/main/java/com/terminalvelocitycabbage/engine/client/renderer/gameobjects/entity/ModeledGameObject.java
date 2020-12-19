package com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModeledGameObject extends EmptyGameObject {

	Model model;

	protected ModeledGameObject() {}

	private ModeledGameObject(Vector3f position, Quaternionf rotation, Vector3f scale, Model model) {
		super(position, rotation, scale);
		this.model = model;
		enable();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		if (render) {
			model.render();
		}
	}

	@Override
	public void update() {
		if (needsUpdate) {
			model.update(position, rotation, scale);
			needsUpdate = false;
		}
	}

	public void destroy() {
		model.destroy();
	}

	public Model getModel() {
		return model;
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
				rotation = new Quaternionf(0, 0, 0, 1);
			}
			if (scale == null) {
				scale = new Vector3f(1, 1, 1);
			}
			return new ModeledGameObject(position, rotation, scale, model);
		}
	}
}
