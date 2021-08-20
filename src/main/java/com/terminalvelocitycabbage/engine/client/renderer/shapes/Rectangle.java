package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.vertexformats.VertexXYZ;

public class Rectangle extends Mesh {

	public Rectangle(VertexXYZ topLeft, VertexXYZ bottomLeft, VertexXYZ bottomRight, VertexXYZ topRight) {
		this.vertices = new VertexXYZ[]{topLeft, bottomLeft, bottomRight, topRight};
		this.vertexOrder = new byte[]{ 0, 1, 2, 2, 3, 0 };
	}
}
