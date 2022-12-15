package com.terminalvelocitycabbage.templates.camera.firstperson;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.input.KeyBind;
import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.Window;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;

public class FirstPersonInputHandler extends InputHandler {

    public static KeyBind FORWARD;
    public static KeyBind BACKWARDS;
    public static KeyBind LEFT;
    public static KeyBind RIGHT;
    public static KeyBind UP;
    public static KeyBind DOWN;

    int xInput = 0;
    int yInput = 0;
    int zInput = 0;

    public FirstPersonInputHandler(Window window) {
        super(window);
    }

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
        if (keyBind.matches(LEFT)) xInput--;
        if (keyBind.matches(RIGHT)) xInput++;
        if (keyBind.matches(UP)) yInput++;
        if (keyBind.matches(DOWN)) yInput--;
        if (keyBind.matches(FORWARD)) zInput--;
        if (keyBind.matches(BACKWARDS)) zInput++;
    }

    public Vector3f getInput() {
        return new Vector3f(xInput, yInput, zInput);
    }

    @Override
    public void resetDeltas() {
        super.resetDeltas();
        xInput = 0;
        yInput = 0;
        zInput = 0;
    }
}
