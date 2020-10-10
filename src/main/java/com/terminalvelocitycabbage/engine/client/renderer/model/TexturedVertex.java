package com.terminalvelocitycabbage.engine.client.renderer.model;

public class TexturedVertex extends ColoredVertex {

	private float[] uv = new float[] {0f, 0f};

	public static final int TEXTURE_ELEMENT_COUNT = 2;

	//The number of bytes required to store each section of vertex information
	public static final int TEXTURE_BYTES = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int TEXTURE_OFFSET = COLOR_OFFSET + COLOR_BYTES;

	public static final int ELEMENT_COUNT = 10;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES + COLOR_BYTES + TEXTURE_BYTES;

	@Override
	public TexturedVertex setXYZ(float x, float y, float z) {
		return (TexturedVertex) super.setXYZ(x, y, z);
	}

	@Override
	public TexturedVertex setXYZW(float x, float y, float z, float w) {
		return (TexturedVertex) super.setXYZW(x, y, z, w);
	}

	@Override
	public TexturedVertex setRGB(int r, int g, int b) {
		return (TexturedVertex) super.setRGB(r, g, b);
	}

	@Override
	public TexturedVertex setRGB(float r, float g, float b) {
		return (TexturedVertex) super.setRGB(r, g, b);
	}

	@Override
	public TexturedVertex setRGBA(float r, float g, float b, float a) {
		return (TexturedVertex) super.setRGBA(r, g, b, a);
	}

	@Override
	public TexturedVertex setRGBA(int r, int g, int b, float a) {
		return (TexturedVertex) super.setRGBA(r, g, b, a);
	}

	public TexturedVertex setUv(float u, float v) {
		uv = new float[] {u, v};
		return this;
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
