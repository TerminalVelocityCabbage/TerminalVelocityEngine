package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;

public class Rectangle extends Mesh {

	public Rectangle(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight) {
		this.vertices = new Vertex[]{topLeft, bottomLeft, bottomRight, topRight};
		this.vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
