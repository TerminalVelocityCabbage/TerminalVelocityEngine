package com.terminalvelocitycabbage.engine.client.renderer.model;

public class Vertex {

	//Vertex information
	private float[] xyz = new float[] {0f, 0f, 0f};
	private float[] rgba = new float[] {1f, 1f, 1f, 1f};
	private float[] uv = new float[] {0f, 0f};

	public static final int ELEMENT_BYTES = Float.BYTES;

	public static final int POSITION_ELEMENT_COUNT = 4;
	public static final int COLOR_ELEMENT_COUNT = 4;
	public static final int TEXTURE_ELEMENT_COUNT = 2;

	//The number of bytes required to store each section of vertex information
	public static final int POSITION_BYTES = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int COLOR_BYTES = COLOR_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int TEXTURE_BYTES = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int POSITION_OFFSET = 0;
	public static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_BYTES;
	public static final int TEXTURE_OFFSET = COLOR_OFFSET + COLOR_BYTES;

	//The total number of elements in this vertex
	//xyzw.length + rgba.length + uv.length
	public static final int ELEMENT_COUNT = 10;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES + COLOR_BYTES + TEXTURE_BYTES;

	public Vertex setXYZ(float x, float y, float z) {
		this.xyz = new float[] {x, y, z};
		return this;
	}

	public Vertex addXYZ(float x, float y, float z) {
		return setXYZ(xyz[0] + x, xyz[1] + y, xyz[2] + z);
	}

	public Vertex setRGBA(float r, float g, float b, float a) {
		rgba = new float[] {r, g, b, a};
		return this;
	}

	public Vertex setRGBA(int r, int g, int b, float a) {
		return setRGBA((float)r/255, (float)g/255, (float)b/255, a);
	}

	public Vertex setRGB(int r, int g, int b) {
		return setRGBA((float)r/255, (float)g/255, (float)b/255, 1.0f);
	}

	public Vertex setRGB(float r, float g, float b) {
		return setRGBA(r, g, b, 1.0f);
	}

	public Vertex setUv(float u, float v) {
		uv = new float[] {u, v};
		return this;
	}

	public float[] getXYZ() {
		return xyz;
	}

	public float[] getRGBA() {
		return rgba;
	}

	public float[] getUV() {
		return uv;
	}

	public static float[] getElements(float[] xyz, float[] rgba, float[] uv) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyz[0];
		out[i++] = xyz[1];
		out[i++] = xyz[2];
		out[i++] = 1;

		out[i++] = rgba[0];
		out[i++] = rgba[1];
		out[i++] = rgba[2];
		out[i++] = rgba[3];

		out[i++] = uv[0];
		out[i++] = uv[1];

		return out;
	}
}
