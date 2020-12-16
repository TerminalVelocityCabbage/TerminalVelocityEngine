package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;

public class TexturedRectangle extends ModelMesh {

	public TexturedRectangle(ModelVertex topLeft, ModelVertex bottomLeft, ModelVertex bottomRight, ModelVertex topRight) {
		this.vertices = new ModelVertex[]{topLeft, bottomLeft, bottomRight, topRight};
		this.vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
