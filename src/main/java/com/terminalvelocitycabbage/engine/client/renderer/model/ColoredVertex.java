package com.terminalvelocitycabbage.engine.client.renderer.model;

public class ColoredVertex extends Vertex {

	protected float[] rgba = new float[] {1f, 1f, 1f, 1f};

	public static final int COLOR_ELEMENT_COUNT = 4;

	//The number of bytes required to store each section of vertex information
	public static final int COLOR_BYTES = COLOR_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int COLOR_OFFSET = POSITION_OFFSET + POSITION_BYTES;

	public static final int ELEMENT_COUNT = 8;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES + COLOR_BYTES;

	public ColoredVertex setRGBA(float r, float g, float b, float a) {
		rgba = new float[] {r, g, b, a};
		return this;
	}

	public ColoredVertex setRGBA(int r, int g, int b, float a) {
		return setRGBA((float)r/255, (float)g/255, (float)b/255, a);
	}

	public ColoredVertex setRGB(int r, int g, int b) {
		return setRGBA((float)r/255, (float)g/255, (float)b/255, 1.0f);
	}

	@Override
	public ColoredVertex setXYZ(float x, float y, float z) {
		return (ColoredVertex) super.setXYZ(x, y, z);
	}

	@Override
	public ColoredVertex setXYZW(float x, float y, float z, float w) {
		return (ColoredVertex) super.setXYZW(x, y, z, w);
	}

	public ColoredVertex setRGB(float r, float g, float b) {
		return setRGBA(r, g, b, 1.0f);
	}

	public float[] getRGBA() {
		return rgba;
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

		return out;
	}
}
