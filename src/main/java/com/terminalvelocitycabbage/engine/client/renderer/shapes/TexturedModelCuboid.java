package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;

public class TexturedModelCuboid {

	public static MeshPart createTexturedCuboid(Vertex frontTL, Vertex frontBL, Vertex frontBR, Vertex frontTR,
												Vertex rightTL, Vertex rightBL, Vertex rightBR, Vertex rightTR,
												Vertex backTL, Vertex backBL, Vertex backBR, Vertex backTR,
												Vertex leftTL, Vertex leftBL, Vertex leftBR, Vertex leftTR,
												Vertex topTL, Vertex topBL, Vertex topBR, Vertex topTR,
												Vertex bottomTL, Vertex bottomBL, Vertex bottomBR, Vertex bottomTR) {
		return new MeshPart(
			new Vertex[]{
				frontTL, frontBL, frontBR, frontTR,
				rightTL, rightBL, rightBR, rightTR,
				backTL, backBL, backBR, backTR,
				leftTL, leftBL, leftBR, leftTR,
				topTL, topBL, topBR, topTR,
				bottomTL, bottomBL, bottomBR, bottomTR,
			},
			new int[]{
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
