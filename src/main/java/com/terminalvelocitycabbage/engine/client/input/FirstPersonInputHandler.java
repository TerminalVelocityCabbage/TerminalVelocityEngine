package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class FirstPersonInputHandler extends InputHandler {

    public static KeyBind FORWARD;
    public static KeyBind BACKWARDS;
    public static KeyBind LEFT;
    public static KeyBind RIGHT;
    public static KeyBind UP;
    public static KeyBind DOWN;

    @Override
    public void init(Window window) {
        super.init(window);
        FORWARD = new KeyBind(GLFW_KEY_W);
        BACKWARDS = new KeyBind(GLFW_KEY_S);
        LEFT = new KeyBind(GLFW_KEY_A);
        RIGHT = new KeyBind(GLFW_KEY_D);
        UP = new KeyBind(GLFW_KEY_SPACE);
        DOWN = new KeyBind(GLFW_KEY_LEFT_SHIFT);
    }

    @Override
    public void processInput(KeyBind keyBind) {
        //No special keybind comparisons required
    }
}
