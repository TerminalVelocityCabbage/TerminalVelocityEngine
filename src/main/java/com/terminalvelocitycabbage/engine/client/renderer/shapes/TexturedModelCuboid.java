package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.vertexformats.VertexXYZNormalUV;

public class TexturedModelCuboid {

	public static ModelMeshPart createTexturedCuboid(VertexXYZNormalUV frontTL, VertexXYZNormalUV frontBL, VertexXYZNormalUV frontBR, VertexXYZNormalUV frontTR,
													 VertexXYZNormalUV rightTL, VertexXYZNormalUV rightBL, VertexXYZNormalUV rightBR, VertexXYZNormalUV rightTR,
													 VertexXYZNormalUV backTL, VertexXYZNormalUV backBL, VertexXYZNormalUV backBR, VertexXYZNormalUV backTR,
													 VertexXYZNormalUV leftTL, VertexXYZNormalUV leftBL, VertexXYZNormalUV leftBR, VertexXYZNormalUV leftTR,
													 VertexXYZNormalUV topTL, VertexXYZNormalUV topBL, VertexXYZNormalUV topBR, VertexXYZNormalUV topTR,
													 VertexXYZNormalUV bottomTL, VertexXYZNormalUV bottomBL, VertexXYZNormalUV bottomBR, VertexXYZNormalUV bottomTR) {
		return new ModelMeshPart(
			new VertexXYZNormalUV[]{
				frontTL, frontBL, frontBR, frontTR,
				rightTL, rightBL, rightBR, rightTR,
				backTL, backBL, backBR, backTR,
				leftTL, leftBL, leftBR, leftTR,
				topTL, topBL, topBR, topTR,
				bottomTL, bottomBL, bottomBR, bottomTR,
			},
			new short[]{
				0, 1, 2, 2, 3, 0,
				4, 5, 6, 6, 7, 4,
				8, 9, 10, 10, 11, 8,
				12, 13, 14, 14, 15, 12,
				16, 17, 18, 18, 19, 16,
				20, 21, 22, 22, 23, 20
			}
		);
	}
}
