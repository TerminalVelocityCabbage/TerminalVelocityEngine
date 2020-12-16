package com.terminalvelocitycabbage.engine.client.renderer.model;

public class Vertex {

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

	public Vertex setXYZ(float x, float y, float z) {
		xyz[0] = x;
		xyz[1] = y;
		xyz[2] = z;
		return this;
	}

	public Vertex addXYZ(float x, float y, float z) {
		xyz[0] += x;
		xyz[1] += y;
		xyz[2] += z;
		return this;
	}

	public float[] getXYZ() {
		return xyz;
	}

	public static float[] getElements(float[] xyz) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyz[0];
		out[i++] = xyz[1];
		out[i++] = xyz[2];

		return out;
	}

	public static float[] getElements(float x, float y, float z, float[] uv, float nx, float ny, float nz) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = x;
		out[i++] = y;
		out[i++] = z;

		out[i++] = uv[0];
		out[i++] = uv[1];

		out[i++] = nx;
		out[i++] = ny;
		out[i++] = nz;

		return out;
	}
}
