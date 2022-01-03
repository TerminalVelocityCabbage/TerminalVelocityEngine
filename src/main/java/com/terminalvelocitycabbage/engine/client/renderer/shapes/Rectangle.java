package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;

public class Rectangle {

	public static MeshPart createRectangle(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight) {
		return new MeshPart(
				new Vertex[]{ topLeft, bottomLeft, bottomRight, topRight },
				new int[] { 0, 1, 2, 2, 3, 0 }
		);
	}
}
