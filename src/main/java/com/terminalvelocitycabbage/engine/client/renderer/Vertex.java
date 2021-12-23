package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderElement;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;

import java.util.Arrays;

public class Vertex {
    private final float[] data;

    protected RenderFormat format = null;

    public Vertex(float[] data) {
        this.data = data;
    }

    public float[] getData() {
        return data;
    }

    public void setFormat(RenderFormat format) {
        this.format = format;
    }

    public float[] grabData(int off, int amount) {
        float[] afloat = new float[amount];
        System.arraycopy(this.data, off, afloat, 0, amount);
        return afloat;
    }

    public float getX() {
        if(this.format != null) {
            return this.data[this.format.getElementOffset(RenderElement.POSITION, 0)];
        }
        return this.data[0];
    }

    public float getY() {
        if(this.format != null) {
            return this.data[this.format.getElementOffset(RenderElement.POSITION, 0)+1];
        }
        return this.data[1];
    }

    public float getZ() {
        if(this.format != null) {
            return this.data[this.format.getElementOffset(RenderElement.POSITION, 0)+2];
        }
        return this.data[2];
    }

    public float[] getXYZ() {
        if(this.format != null) {
            int o = this.format.getElementOffset(RenderElement.POSITION, 0);
            return new float[] {
                this.data[o], this.data[o+1], this.data[o+2]
            };
        }
        return new float[] {
            this.data[0], this.data[1], this.data[2]
        };
    }

    public void setXYZ(float x, float y, float z) {
        if(this.format != null) {
            int off = this.format.getElementOffset(RenderElement.POSITION, 0);
            this.data[off] = x;
            this.data[off+1] = y;
            this.data[off+2] = z;
        }
    }

    public void setNormalXYZ(float x, float y, float z) {
        if(this.format != null) {
            int off = this.format.getElementOffset(RenderElement.NORMAL, 0);
            this.data[off] = x;
            this.data[off+1] = y;
            this.data[off+2] = z;
        }
    }

    public void addXYZ(float x, float y, float z) {
        if(this.format != null) {
            int off = this.format.getElementOffset(RenderElement.POSITION, 0);
            this.data[off] += x;
            this.data[off+1] += y;
            this.data[off+2] += z;
        }
    }

    public static Vertex position(float x, float y, float z) {
        return new Vertex(new float[]{ x, y, z });
    }

    public static Vertex positionUv(float x, float y, float z, float u, float v) {
        return new Vertex(new float[]{ x, y, z, u, v });
    }

    public static Vertex positionUvNormal(float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        return new Vertex(new float[]{ x, y, z, u, v, nx, ny, nz });
    }

    public static Vertex positionUvColour(float x, float y, float z, float u, float v, float r, float g, float b, float a) {
        return new Vertex(new float[]{ x, y, z, u, v, r, g, b, a });
    }

    public static Vertex positionUvNormalColour(float x, float y, float z, float u, float v,  float nx, float ny, float nz, float r, float g, float b, float a) {
        return new Vertex(new float[]{ x, y, z, u, v, nx, ny, nz, r, g, b, a });
    }

    public static Vertex positionColour(float x, float y, float z, float r, float g, float b, float a) {
        return new Vertex(new float[] { x, y, z, r, g, b, a });
    }

    public static Vertex ui(float x, float y, float z, float u, float v, float r, float g, float b, float a, float borderRadius) {
        return new Vertex(new float[]{ x, y, z, u, v, r, g, b, a, borderRadius });
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "data=" + Arrays.toString(data) +
                ", format=" + format +
                '}';
    }
}
