package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextVertex;

public class TextRectangle {
	private final TextVertex[] vertices;
	private final short[] vertexOrder;

	private TextRectangle(TextVertex[] vertices, short[] vertexOrder) {
		this.vertices = vertices;
		this.vertexOrder = vertexOrder;
	}

	public TextVertex[] getVertices() {
		return vertices;
	}

	public short[] getVertexOrder() {
		return vertexOrder;
	}

	public static TextRectangle createTextRectangle(TextVertex topLeft, TextVertex bottomLeft, TextVertex bottomRight, TextVertex topRight) {
		return new TextRectangle(
			new TextVertex[]{topLeft, bottomLeft, bottomRight, topRight},
			new short[]{ 0, 1, 2, 2, 3, 0 }
		);
	}
}
