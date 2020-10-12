package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.TexturedMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import org.joml.Vector3f;

public class TexturedRectangle extends TexturedMesh {

	public TexturedRectangle(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight, ResourceManager resourceManager, Identifier texture) {
		super(new Vector3f(0, 0, 0), resourceManager, texture);
		vertices = new Vertex[]{topLeft, bottomLeft, bottomRight, topRight};
		vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
