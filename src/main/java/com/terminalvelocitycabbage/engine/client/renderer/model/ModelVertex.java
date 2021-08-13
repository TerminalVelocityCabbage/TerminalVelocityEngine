package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.Arrays;

public class ModelVertex extends Vertex {

	private float[] uv = new float[] {0f, 0f};
	private float[] normal = new float[] {0f, 0f, 0f};

	public static final int TEXTURE_ELEMENT_COUNT = 2;
	public static final int NORMAL_ELEMENT_COUNT = 3;

	public static final int TEXTURE_BYTES = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int NORMAL_BYTES = NORMAL_ELEMENT_COUNT * ELEMENT_BYTES;

	public static final int TEXTURE_OFFSET = POSITION_OFFSET + POSITION_BYTES;
	public static final int NORMAL_OFFSET = TEXTURE_OFFSET + TEXTURE_BYTES;

	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT + TEXTURE_ELEMENT_COUNT + NORMAL_ELEMENT_COUNT;

	public static final int STRIDE = POSITION_BYTES + TEXTURE_BYTES + NORMAL_BYTES;

	public ModelVertex setUv(float u, float v) {
		uv = new float[] {u, v};
		return this;
	}

	public ModelVertex setNormal(float x, float y, float z) {
		normal = new float[] {x, y, z};
		return this;
	}

	@Override
	public ModelVertex setXYZ(float x, float y, float z) {
		return (ModelVertex) super.setXYZ(x, y, z);
	}

	public float[] getUV() {
		return uv;
	}

	public float[] getNormals() {
		return normal;
	}

	public static float[] getElements(float[] xyz, float[] uv, float[] normals) {
		float[] out = new float[ELEMENT_COUNT];
		int i = 0;

		out[i++] = xyz[0];
		out[i++] = xyz[1];
		out[i++] = xyz[2];

		out[i++] = uv[0];
		out[i++] = uv[1];

		out[i++] = normals[0];
		out[i++] = normals[1];
		out[i++] = normals[2];

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

	public void printInfo() {
		Log.info("Positions:");
		Log.info(Arrays.toString(getXYZ()));
		Log.info("Normals:");
		Log.info(Arrays.toString(getNormals()));
		Log.info("UVs:");
		Log.info(Arrays.toString(getUV()));
	}
}
