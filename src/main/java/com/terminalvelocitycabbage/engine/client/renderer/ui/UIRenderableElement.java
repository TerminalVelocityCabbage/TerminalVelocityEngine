package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;

import java.util.Collections;

public abstract class UIRenderableElement {

	boolean needsUpdate;
	Model rectangle;
	int zIndex;

	public UIRenderableElement() {
		this.zIndex = 0;
		this.needsUpdate = false;
		this.rectangle = new Model(Collections.singletonList(new Model.Part(
			new TexturedRectangle(
					new ModelVertex().setXYZ(0, 0, 0),
					new ModelVertex().setXYZ(0, 0, 0),
					new ModelVertex().setXYZ(0, 0, 0),
					new ModelVertex().setXYZ(0, 0, 0)
			))))
		.setMaterial(Material.builder().color(0, 0, 0, 0).build());
	}

	public void bind() {
		rectangle.bind();
	}

	public abstract void update();

	public void render() {
		rectangle.render();
	}

	public void queueUpdate() {
		this.needsUpdate = true;
	}

}
