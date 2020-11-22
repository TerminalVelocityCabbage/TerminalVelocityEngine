package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBind {

	long window;
	int keyCode;
	int scancode;
	int action;
	int modifiers;

	public static final int ANY = -1;
	public static final int NONE = 0;

	public KeyBind(int key) {
		this.window = Renderer.getWindow().getID();
		this.keyCode = key;
		this.scancode = ANY;
		this.action = ANY;
		this.modifiers = NONE;
	}

	public KeyBind(int key, int scancode, int action, int modifiers) {
		this.window = Renderer.getWindow().getID();
		this.keyCode = key;
		this.scancode = scancode;
		this.action = action;
		this.modifiers = modifiers;
	}

	public boolean isKeyPressed(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.getKeyCode()) == GLFW_PRESS;
	}

	public boolean isKeyReleased(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.getKeyCode()) == GLFW_RELEASE;
	}

	public boolean isKeyRepeated(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.getKeyCode()) == GLFW_REPEAT;
	}

	public long getWindow() {
		return window;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public int getScancode() {
		return scancode;
	}

	public int getAction() {
		return action;
	}

	public int getModifiers() {
		return modifiers;
	}

	public boolean equalsKeyAndAction(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KeyBind keyBind = (KeyBind) o;
		return keyCode == keyBind.keyCode &&
				action == keyBind.action;
	}

	public boolean equalsKey(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KeyBind keyBind = (KeyBind) o;
		return keyCode == keyBind.keyCode;
	}
}
