package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.*;

public class TextRectangle {
	public static TextMeshPart createTextRectangle(TextVertex topLeft, TextVertex bottomLeft, TextVertex bottomRight, TextVertex topRight) {
		return new TextMeshPart(new TextVertex[]{topLeft, bottomLeft, bottomRight, topRight}, new short[]{ 0, 1, 2, 2, 3, 0 });
	}
}
