package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;

public class Rectangle {

	public static Model.Part simpleRectangle(Vertex topLeft, Vertex bottomLeft, Vertex bottomRight, Vertex topRight) {
		return new Model.Part(
			new MeshPart(
				new Vertex[]{ topLeft, bottomLeft, bottomRight, topRight },
				new short[] { 0, 1, 2, 2, 3, 0 }
			)
		);
	}
}
