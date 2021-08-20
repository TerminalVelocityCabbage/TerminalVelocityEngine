package com.terminalvelocitycabbage.engine.client.renderer.model.vertexformats;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Math;
import org.joml.Vector3f;

import java.util.Arrays;

public class VertexXYZNormalUVRGB extends VertexXYZNormalUV {

    private float[] color = new float[] {0f, 0f, 0f};

    public static final int COLOR_ELEMENT_COUNT = 3;

    public static final int COLOR_BYTES = COLOR_ELEMENT_COUNT * ELEMENT_BYTES;

    public static final int COLOR_OFFSET = NORMAL_OFFSET + COLOR_BYTES;

    public static final int ELEMENT_COUNT = POSITION_ELEMENT_COUNT + TEXTURE_ELEMENT_COUNT + NORMAL_ELEMENT_COUNT + COLOR_ELEMENT_COUNT;

    public static final int STRIDE = POSITION_BYTES + TEXTURE_BYTES + NORMAL_BYTES + COLOR_BYTES;

    public VertexXYZNormalUVRGB setColor(int r, int g, int b) {
        color = new float[] {r/255f, g/255f, b/255f};
        return this;
    }

    public VertexXYZNormalUVRGB setColor(Vector3f color) {
        this.color = new float[] {color.x(), color.y(), color.z()};
        return this;
    }

    @Override
    public VertexXYZNormalUVRGB setXYZ(float x, float y, float z) {
        return (VertexXYZNormalUVRGB) super.setXYZ(x, y, z);
    }

    @Override
    public VertexXYZNormalUVRGB setUv(float u, float v) {
        return (VertexXYZNormalUVRGB) super.setUv(u, v);
    }

    @Override
    public VertexXYZNormalUVRGB setNormal(float x, float y, float z) {
        return (VertexXYZNormalUVRGB) super.setNormal(x, y, z);
    }

    public float[] getColor() {
        return color;
    }

    public static float[] getElements(float[] xyz, float[] uv, float[] normals, float[] color) {
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

        out[i++] = color[0];
        out[i++] = color[1];
        out[i++] = color[2];

        return out;
    }

    public static float[] getElements(float x, float y, float z, float[] uv, float nx, float ny, float nz, float cr, float cg, float cb) {
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

        out[i++] = cr;
        out[i++] = cg;
        out[i++] = cb;

        return out;
    }

    public void printInfo() {
        Log.info("Positions:");
        Log.info(Arrays.toString(getXYZ()));
        Log.info("Normals:");
        Log.info(Arrays.toString(getNormals()));
        Log.info("UVs:");
        Log.info(Arrays.toString(getUV()));
        Log.info("Color:");
        Log.info(Arrays.toString(getColor()));
    }
}
