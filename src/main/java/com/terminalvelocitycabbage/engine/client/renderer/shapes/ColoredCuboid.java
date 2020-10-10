package com.terminalvelocitycabbage.engine.client.renderer.shapes;

import com.terminalvelocitycabbage.engine.client.renderer.model.ColoredVertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;

public class ColoredCuboid extends Mesh {
	public ColoredCuboid(ColoredVertex topLeftF, ColoredVertex bottomLeftF, ColoredVertex bottomRightF, ColoredVertex topRightF,
						 ColoredVertex topLeftR, ColoredVertex bottomLeftR, ColoredVertex bottomRightR, ColoredVertex topRightR) {
		vertices = new ColoredVertex[]{topLeftF, bottomLeftF, bottomRightF, topRightF, topLeftR, bottomLeftR, bottomRightR, topRightR};
		vertexOrder = new byte[]{
				3, 0, 2, 0, 1, 2,	//Front			4---7
				7, 3, 6, 3, 2, 6,	//Right		   /|  /|
				7, 5, 4, 7, 6, 5,	//Rear		  0---3 |
				0, 4, 1, 4, 5, 1,	//Left        | 5-|-6
				4, 0, 3, 3, 7, 4,	//Top		  |/  |/
				5, 6, 1, 6, 2, 1};	//Bottom	  1---2
	}
}
