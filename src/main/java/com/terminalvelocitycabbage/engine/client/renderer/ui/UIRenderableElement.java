package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.Rectangle;

public abstract class UIRenderableElement {

	boolean needsUpdate;
	Rectangle rectangle;
	int zIndex;

	public UIRenderableElement() {
		this.zIndex = 0;
		this.needsUpdate = false;
		this.rectangle = new Rectangle(new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0), new Vertex().setXYZ(0, 0, 0));
	}

	public void bind() {
		rectangle.bind();
	}

	public abstract void update();

	public void render() {
		rectangle.render();
	}

	public void destroy() {
		rectangle.destroy();
	}

	public void queueUpdate() {
		this.needsUpdate = true;
	}

}
