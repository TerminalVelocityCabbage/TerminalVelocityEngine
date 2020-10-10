package com.terminalvelocitycabbage.engine.client.renderer.model;

public class Vertex {

	//Vertex information
	private float[] xyzw = new float[] {0f, 0f, 0f, 1f};
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

	public float[] getXYZW() {
		return xyzw;
	}

	public float[] getRGBA() {
		return rgba;
	}

	public float[] getUV() {
		return uv;
	}

	public float[] getElements() {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyzw[0];
		out[i++] = xyzw[1];
		out[i++] = xyzw[2];
		out[i++] = xyzw[3];

		out[i++] = rgba[0];
		out[i++] = rgba[1];
		out[i++] = rgba[2];
		out[i++] = rgba[3];

		out[i++] = uv[0];
		out[i++] = uv[1];

		return out;
	}
}
