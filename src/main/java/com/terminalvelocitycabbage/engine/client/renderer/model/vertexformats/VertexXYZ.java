package com.terminalvelocitycabbage.engine.client.renderer.model.vertexformats;

public class VertexXYZ {

	//Vertex information
	private float[] xyz = new float[] {0f, 0f, 0f};

	public static final int ELEMENT_BYTES = Float.BYTES;

	public static final int POSITION_ELEMENT_COUNT = 3;

	//The number of bytes required to store each section of vertex information
	public static final int POSITION_BYTES = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int POSITION_OFFSET = 0;

	//The total number of elements in this vertex
	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES;

	public VertexXYZ setXYZ(float x, float y, float z) {
		xyz[0] = x;
		xyz[1] = y;
		xyz[2] = z;
		return this;
	}

	public VertexXYZ addXYZ(float x, float y, float z) {
		xyz[0] += x;
		xyz[1] += y;
		xyz[2] += z;
		return this;
	}

	public float[] getXYZ() {
		return xyz;
	}

	public float getX() {
		return xyz[0];
	}

	public float getY() {
		return xyz[1];
	}

	public float getZ() {
		return xyz[2];
	}

	public static float[] getElements(float[] xyz) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyz[0];
		out[i++] = xyz[1];
		out[i++] = xyz[2];

		return out;
	}
}
