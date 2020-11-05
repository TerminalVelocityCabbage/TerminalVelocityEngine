package com.terminalvelocitycabbage.engine.client.renderer.model;

public class Vertex {

	//Vertex information
	private float[] xyz = new float[] {0f, 0f, 0f};
	private float[] uv = new float[] {0f, 0f};
	private float[] normal = new float[] {0f, 0f, 0f};

	public static final int ELEMENT_BYTES = Float.BYTES;

	public static final int POSITION_ELEMENT_COUNT = 3;
	public static final int TEXTURE_ELEMENT_COUNT = 2;
	public static final int NORMAL_ELEMENT_COUNT = 3;

	//The number of bytes required to store each section of vertex information
	public static final int POSITION_BYTES = POSITION_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int TEXTURE_BYTES = TEXTURE_ELEMENT_COUNT * ELEMENT_BYTES;
	public static final int NORMAL_BYTES = NORMAL_ELEMENT_COUNT * ELEMENT_BYTES;

	//The index offset for the beginning of each section of vertex information
	public static final int POSITION_OFFSET = 0;
	public static final int TEXTURE_OFFSET = POSITION_OFFSET + POSITION_BYTES;
	public static final int NORMAL_OFFSET = TEXTURE_OFFSET + TEXTURE_BYTES;

	//The total number of elements in this vertex
	public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT + TEXTURE_ELEMENT_COUNT + NORMAL_ELEMENT_COUNT;

	//The total number of bytes this vertex requires
	public static final int STRIDE = POSITION_BYTES + TEXTURE_BYTES + NORMAL_BYTES;

	public Vertex setXYZ(float x, float y, float z) {
		this.xyz = new float[] {x, y, z};
		return this;
	}

	public Vertex addXYZ(float x, float y, float z) {
		return setXYZ(xyz[0] + x, xyz[1] + y, xyz[2] + z);
	}

	public Vertex setUv(float u, float v) {
		uv = new float[] {u, v};
		return this;
	}

	public Vertex setNormal(float x, float y, float z) {
		normal = new float[] {x, y, z};
		return this;
	}

	public float[] getXYZ() {
		return xyz;
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
}
