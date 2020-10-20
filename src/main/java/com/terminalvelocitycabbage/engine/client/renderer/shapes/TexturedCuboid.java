package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;

public class TexturedCuboid extends Mesh {
	public TexturedCuboid(Vertex frontTL, Vertex frontBL, Vertex frontBR, Vertex frontTR,
						  Vertex rightTL, Vertex rightBL, Vertex rightBR, Vertex rightTR,
						  Vertex backTL, Vertex backBL, Vertex backBR, Vertex backTR,
						  Vertex leftTL, Vertex leftBL, Vertex leftBR, Vertex leftTR,
						  Vertex topTL, Vertex topBL, Vertex topBR, Vertex topTR,
						  Vertex bottomTL, Vertex bottomBL, Vertex bottomBR, Vertex bottomTR,
						  Material material) {
		this.material = material;
		this.vertices = new Vertex[]{
				frontTL, frontBL, frontBR, frontTR,
				rightTL, rightBL, rightBR, rightTR,
				backTL, backBL, backBR, backTR,
				leftTL, leftBL, leftBR, leftTR,
				topTL, topBL, topBR, topTR,
				bottomTL, bottomBL, bottomBR, bottomTR,
				};
		this.vertexOrder = new byte[]{
				0, 1, 2, 2, 3, 0,
				4, 5, 6, 6, 7, 4,
				8, 9, 10, 10, 11, 8,
				12, 13, 14, 14, 15, 12,
				16, 17, 18, 18, 19, 16,
				20, 21, 22, 22, 23, 20
		};
	}
}
