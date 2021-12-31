package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

import com.terminalvelocitycabbage.engine.client.renderer.ui.AnimatableUIValue;

public class TextStyle {

    AnimatableUIValue colorRed;
    AnimatableUIValue colorGreen;
    AnimatableUIValue colorBlue;
    AnimatableUIValue colorAlpha;
    private float size;

    //TODO convert to proper builder like UIRenderable
    public TextStyle() {
        colorRed = new AnimatableUIValue(1);
        colorGreen = new AnimatableUIValue(1);
        colorBlue = new AnimatableUIValue(1);
        colorAlpha = new AnimatableUIValue(1);
        this.size = -1f;
    }

    public float getRed() {
        return colorRed.getValue();
    }

    public float getGreen() {
        return colorGreen.getValue();
    }

    public float getBlue() {
        return colorBlue.getValue();
    }

    public float getAlpha() {
        return colorAlpha.getValue();
    }

    public TextStyle setBaseColor(float r, float g, float b, float a) {
        colorRed = new AnimatableUIValue(r);
        colorGreen = new AnimatableUIValue(g);
        colorBlue = new AnimatableUIValue(b);
        colorAlpha = new AnimatableUIValue(a);
        return this;
    }

    public void setColorTarget(float r, float g, float b, float a) {
        colorRed.setTarget(r);
        colorGreen.setTarget(g);
        colorBlue.setTarget(b);
        colorAlpha.setTarget(a);
    }

    public void resetColor() {
        colorRed.unsetTarget();
        colorGreen.unsetTarget();
        colorBlue.unsetTarget();
        colorAlpha.unsetTarget();
    }

    public float getSize() {
        return size;
    }

    public TextStyle setSize(float size) {
        this.size = size;
        return this;
    }
}
