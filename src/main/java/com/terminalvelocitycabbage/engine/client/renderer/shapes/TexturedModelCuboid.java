package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ModelMeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;

public class TexturedModelCuboid {

	public static ModelMeshPart createTexturedCuboid(ModelVertex frontTL, ModelVertex frontBL, ModelVertex frontBR, ModelVertex frontTR,
													 ModelVertex rightTL, ModelVertex rightBL, ModelVertex rightBR, ModelVertex rightTR,
													 ModelVertex backTL, ModelVertex backBL, ModelVertex backBR, ModelVertex backTR,
													 ModelVertex leftTL, ModelVertex leftBL, ModelVertex leftBR, ModelVertex leftTR,
													 ModelVertex topTL, ModelVertex topBL, ModelVertex topBR, ModelVertex topTR,
													 ModelVertex bottomTL, ModelVertex bottomBL, ModelVertex bottomBR, ModelVertex bottomTR) {
		return new ModelMeshPart(
			new ModelVertex[]{
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
