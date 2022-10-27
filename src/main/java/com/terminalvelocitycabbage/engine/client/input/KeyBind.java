package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;

import static org.lwjgl.glfw.GLFW.*;

public class KeyBind {

	Window window;
	int keyCode;
	int scancode;
	int action;
	int modifiers;

	public static final int ANY = -1;
	public static final int NONE = 0;

	public KeyBind(int key) {
		this.window = Renderer.getWindow();
		this.keyCode = key;
		this.scancode = ANY;
		this.action = ANY;
		this.modifiers = NONE;
	}

	public KeyBind(int key, int scancode, int action, int modifiers) {
		this.window = Renderer.getWindow();
		this.keyCode = key;
		this.scancode = scancode;
		this.action = action;
		this.modifiers = modifiers;
	}

	public boolean isKeyPressed() {
		return glfwGetKey(window.getID(), this.getKeyCode()) == GLFW_PRESS;
	}

	public boolean isKeyReleased() {
		return glfwGetKey(window.getID(), this.getKeyCode()) == GLFW_RELEASE;
	}

	public boolean isKeyRepeated() {
		return glfwGetKey(window.getID(), this.getKeyCode()) == GLFW_REPEAT;
	}

	public Window getWindow() {
		return window;
	}

	public long getWindowID() {
		return window.getID();
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
