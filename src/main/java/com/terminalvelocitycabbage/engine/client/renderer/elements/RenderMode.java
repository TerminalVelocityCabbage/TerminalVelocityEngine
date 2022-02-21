package com.terminalvelocitycabbage.engine.client.renderer.elements;

import static org.lwjgl.opengl.GL11.*;

public class RenderMode {

    Modes mode;
    float width;

    public RenderMode() {
        this(Modes.TRIANGLES, 1.0f);
    }

    public RenderMode(Modes mode) {
        this(mode, 1.0f);
    }

    public RenderMode(Modes mode, float width) {
        this.mode = mode;
        this.width = width;
    }

    public Modes getMode() {
        return mode;
    }

    public void setMode(Modes mode) {
        this.mode = mode;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getGlType() {
        return mode.glType;
    }

    public void applyWidth() {
        //TODO these don't work so we should just remove them and implement our own methods for viewing these
        glLineWidth(width);
        glPointSize(width);
    }

    public enum Modes {

        POINTS("points", GL_POINTS),
        LINES("lines", GL_LINES),
        TRIANGLES("triangles", GL_TRIANGLES);

        private String name;
        private int glType;

        Modes(String name, int glType) {
            this.name = name;
            this.glType = glType;
        }

        public String getName() {
            return name;
        }

        public int getGlType() {
            return glType;
        }
    }
}
