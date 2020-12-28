package com.terminalvelocitycabbage.engine.client.renderer.model;

public class TextVertex extends Vertex {

	private float[] uv = new float[] {0f, 0f};
	private float[] color = new float[] {0f, 0f, 0f, 0f};

	public static final int TEXTURE_ELEMENT_COUNT = 2;
	public static final int COLOR_ELEMENT_COUNT = 4;

	public static final int TEXTURE_BYTES = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int COLOR_BYTES = COLOR_ELEMENT_COUNT * ELEMENT_BYTES;

	public static final int TEXTURE_OFFSET = POSITION_OFFSET + POSITION_BYTES;
	public static final int COLOR_OFFSET = TEXTURE_OFFSET + TEXTURE_BYTES;

	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT + TEXTURE_ELEMENT_COUNT + COLOR_ELEMENT_COUNT;

	public static final int STRIDE = POSITION_BYTES + TEXTURE_BYTES + COLOR_BYTES;

	public TextVertex setUv(float u, float v) {
		uv = new float[] {u, v};
		return this;
	}

	public TextVertex setNormal(float r, float g, float b, float a) {
		color = new float[] {r, g, b, a};
		return this;
	}

	@Override
	public TextVertex setXYZ(float x, float y, float z) {
		return (TextVertex) super.setXYZ(x, y, z);
	}

	public float[] getUV() {
		return uv;
	}

	public float[] getColor() {
		return color;
	}

	public static float[] getElements(float[] xyz, float[] uv, float[] color) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyz[0];
		out[i++] = xyz[1];
		out[i++] = xyz[2];

		out[i++] = uv[0];
		out[i++] = uv[1];

		out[i++] = color[0];
		out[i++] = color[1];
		out[i++] = color[2];
		out[i++] = color[3];

		return out;
	}

	public static float[] getElements(float x, float y, float z, float[] uv, float r, float g, float b, float a) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = x;
		out[i++] = y;
		out[i++] = z;

		out[i++] = uv[0];
		out[i++] = uv[1];

		out[i++] = r;
		out[i++] = g;
		out[i++] = b;
		out[i++] = a;

		return out;
	}
}
