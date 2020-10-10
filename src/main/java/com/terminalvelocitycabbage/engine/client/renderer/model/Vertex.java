package com.terminalvelocitycabbage.engine.client.renderer.model;

public abstract class Vertex {

	//Vertex information
	protected float[] xyzw = new float[] {0f, 0f, 0f, 1f};

	public static final int ELEMENT_BYTES = Float.BYTES;

	public static final int POSITION_ELEMENT_COUNT = 4;

	//The number of bytes required to store each section of vertex information
	public static final int POSITION_BYTES = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int POSITION_OFFSET = 0;

	public static final int ELEMENT_COUNT = 4;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES;

	public Vertex setXYZW(float x, float y, float z, float w) {
		this.xyzw = new float[] {x, y, z, w};
		return this;
	}

	public Vertex setXYZ(float x, float y, float z) {
		return setXYZW(x, y, z, 1.0f);
	}

	public Vertex addXYZW(float x, float y, float z, float w) {
		return setXYZW(xyzw[0] + x, xyzw[1] + y, xyzw[2] + z, xyzw[3] + w);
	}

	public float[] getXYZW() {
		return xyzw;
	}

	public float[] getElements() {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyzw[0];
		out[i++] = xyzw[1];
		out[i++] = xyzw[2];
		out[i++] = xyzw[3];

		return out;
	}

}
