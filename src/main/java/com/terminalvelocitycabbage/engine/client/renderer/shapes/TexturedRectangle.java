package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.vertexformats.VertexXYZNormalUV;

public class TexturedRectangle {
	public static ModelMeshPart createTexturedRectangle(VertexXYZNormalUV topLeft, VertexXYZNormalUV bottomLeft, VertexXYZNormalUV bottomRight, VertexXYZNormalUV topRight) {
		return new ModelMeshPart(new VertexXYZNormalUV[]{topLeft, bottomLeft, bottomRight, topRight}, new short[]{ 0, 1, 2, 2, 3, 0 });
	}
}
