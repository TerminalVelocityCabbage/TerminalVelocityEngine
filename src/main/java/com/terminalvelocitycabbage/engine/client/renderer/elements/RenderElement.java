package com.terminalvelocitycabbage.engine.client.renderer.elements;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.util.function.BiConsumer;

public class RenderElement {

    private static final Vector4f REUSABLE = new Vector4f();
    private static final Quaternionf REUSABLE_QUATERNION = new Quaternionf();

    public static final RenderElement POSITION = new RenderElement("position", 3, Float.BYTES).setVec4Transformer(Vector4f::mul);
    public static final RenderElement UV = new RenderElement("uv", 2, Float.BYTES);
    public static final RenderElement NORMAL = new RenderElement("normal", 3, Float.BYTES).setVec4Transformer((vector4f, matrix4f) -> vector4f.rotate(matrix4f.getUnnormalizedRotation(REUSABLE_QUATERNION)));
    public static final RenderElement COLOUR_RGBA = new RenderElement("colour", 4, Float.BYTES);

    //UI stuff
    public static final RenderElement BORDER_RADIUS = new RenderElement("borderRadius", 2, Float.BYTES);
    public static final RenderElement BORDER_THICKNESS = new RenderElement("borderThickness", 2, Float.BYTES);

    private final String debugName;
    private final int count;
    private final int bytes;

    private BiConsumer<float[], Matrix4f> transformer;

    public RenderElement(String debugName, int count, int bytes) {
        this.debugName = debugName;
        this.count = count;
        this.bytes = bytes;
    }

    private RenderElement setVec4Transformer(BiConsumer<Vector4f, Matrix4f> transformer) {
        this.transformer = (floats, matrix4f) -> {
            REUSABLE.set(floats[0], floats[1], floats[2], 1F);
            transformer.accept(REUSABLE, matrix4f);
            floats[0] = REUSABLE.x;
            floats[1] = REUSABLE.y;
            floats[2] = REUSABLE.z;
        };
        return this;
    }

    public boolean hasTransformer() {
        return this.transformer != null;
    }

    public void transform(float[] data, Matrix4f transformation) {
        this.transformer.accept(data, transformation);
    }

    private RenderElement setTransform(BiConsumer<float[], Matrix4f> transformer) {
        this.transformer = transformer;
        return this;
    }

    public int getSize() {
        return this.count * this.bytes;
    }

    public int getCount() {
        return count;
    }
}
