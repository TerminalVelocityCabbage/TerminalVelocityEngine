package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.TexturedMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;

public class TexturedRectangle extends TexturedMesh {

	public TexturedRectangle(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight) {
		vertices = new Vertex[]{topLeft, bottomLeft, bottomRight, topRight};
		vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
