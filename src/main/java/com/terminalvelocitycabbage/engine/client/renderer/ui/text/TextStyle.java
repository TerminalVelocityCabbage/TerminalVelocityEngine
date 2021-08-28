package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

import org.joml.Vector4f;

public class TextStyle {

    private Vector4f color;
    private float size;

    public TextStyle() {
        this.color = new Vector4f(1, 1, 1, 1);
        this.size = -1f;
    }

    public Vector4f getColor() {
        return color;
    }

    public TextStyle setColor(Vector4f color) {
        this.color = color;
        return this;
    }

    public float getSize() {
        return size;
    }

    public TextStyle setSize(float size) {
        this.size = size;
        return this;
    }
}
