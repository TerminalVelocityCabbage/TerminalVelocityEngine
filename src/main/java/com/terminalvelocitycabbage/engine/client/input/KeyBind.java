package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public record KeyBind(long window, int keyCode, int scancode, int action, int modifiers) {

    public KeyBind(int keyCode, int scancode, int action, int modifiers) {
        this(Renderer.getWindow().getID(), keyCode, scancode, action, modifiers);
    }

    public KeyBind(int keyCode, int action, int modifiers) {
        this(Renderer.getWindow().getID(), keyCode, Scancodes.ANY, action, modifiers);
    }

    public KeyBind(int keyCode, int modifiers) {
        this(Renderer.getWindow().getID(), keyCode, Scancodes.ANY, Actions.PRESS, modifiers);
    }

    public KeyBind(int keyCode) {
        this(Renderer.getWindow().getID(), keyCode, Scancodes.ANY, Actions.PRESS, Modifiers.NONE);
    }

    public boolean isKeyPressed() {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased() {
        return glfwGetKey(window, keyCode) == GLFW_RELEASE;
    }

    public boolean isKeyRepeated() {
        return glfwGetKey(window, keyCode) == GLFW_REPEAT;
    }


    public boolean equalsKeyAction(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode && action == keyBind.action;
    }

    public boolean equalsKeyModifiersAction(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode && action == keyBind.action && modifiers == keyBind.modifiers;
    }

    public boolean equalsKey(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyBind keyBind = (KeyBind) o;
        return window == keyBind.window && keyCode == keyBind.keyCode;
    }

    //TODO make enum instead
    public static class Actions {
        public static final int PRESS = 0;
        public static final int RELEASE = 1;
    }

    //TODO make enum instead
    public static class Scancodes {
        public static final int NONE = 0;
        public static final int ANY = 1;
    }

    //TODO make enum instead
    //To combine modifiers just add them together
    public static class Modifiers {
        public static final byte NONE = 0;
        public static final byte SHIFT = 1;
        public static final byte CONTROL = 2;
        public static final byte ALT = 4;
        public static final byte SUPER = 8;
        public static final byte CAPS_LOCK = 16;
        public static final byte NUM_LOCK = 32;
    }
}
