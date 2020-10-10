package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.TexturedMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.TexturedVertex;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

public class TexturedRectangle extends TexturedMesh {

	public TexturedRectangle(TexturedVertex topLeft, TexturedVertex bottomLeft, TexturedVertex bottomRight, TexturedVertex topRight, ResourceManager resourceManager, Identifier texture) {
		super(resourceManager, texture);
		vertices = new TexturedVertex[]{topLeft, bottomLeft, bottomRight, topRight};
		vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
