package com.terminalvelocitycabbage.engine.client.renderer.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ModeledGameObject extends EmptyGameObject {

	Model model;

	public ModeledGameObject() {
		super();
		enable();
	}

	public ModeledGameObject(Model model) {
		super();
		this.model = model;
		enable();
	}

	public ModeledGameObject(Vector3f position, Quaternionf rotation, Vector3f scale, Model model) {
		super(position, rotation, scale);
		this.model = model;
		enable();
	}

	public ModeledGameObject setModel(Model model) {
		this.model = model;
		return this;
	}

	public Model getModel() {
		return model;
	}

	public ModeledGameObject bind() {
		if (model != null) {
			model.bind();
		} else {
			Log.crash("Model Bind Error", new RuntimeException("Could not bind ModeledGameObject without model."));
		}
		return this;
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
}
