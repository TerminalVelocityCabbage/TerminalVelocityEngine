package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;

public class TexturedRectangle {
	public static MeshPart createTexturedRectangle(ModelVertex topLeft, ModelVertex bottomLeft, ModelVertex bottomRight, ModelVertex topRight) {
		return new MeshPart(new ModelVertex[]{topLeft, bottomLeft, bottomRight, topRight}, new short[]{ 0, 1, 2, 2, 3, 0 });
	}
}
