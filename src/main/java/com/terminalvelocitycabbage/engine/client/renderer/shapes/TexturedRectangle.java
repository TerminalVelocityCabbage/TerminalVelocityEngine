package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;

public class TexturedRectangle {
	public static ModelMeshPart createTexturedRectangle(ModelVertex topLeft, ModelVertex bottomLeft, ModelVertex bottomRight, ModelVertex topRight) {
		return new ModelMeshPart(new ModelVertex[]{topLeft, bottomLeft, bottomRight, topRight}, new short[]{ 0, 1, 2, 2, 3, 0 });
	}
}
