package com.terminalvelocitycabbage.templates.camera.firstperson;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.Window;

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
        FORWARD = KeyBind.builder().key(GLFW_KEY_W).build();
        BACKWARDS = KeyBind.builder().key(GLFW_KEY_S).build();
        LEFT = KeyBind.builder().key(GLFW_KEY_A).build();
        RIGHT = KeyBind.builder().key(GLFW_KEY_D).build();
        UP = KeyBind.builder().key(GLFW_KEY_SPACE).build();
        DOWN = KeyBind.builder().key(GLFW_KEY_LEFT_SHIFT).build();
    }

    @Override
    public void processInput(KeyBind keyBind) {
        //No special keybind comparisons required
    }
}
