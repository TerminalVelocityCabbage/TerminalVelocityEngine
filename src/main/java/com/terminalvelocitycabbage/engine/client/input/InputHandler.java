package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public abstract class InputHandler {

	private static long window;

	private Vector2d previousPos;
	protected final Vector2f deltaMouseVector;
	protected final Vector2i deltaScrollVector;

	private boolean focused = false;

	private boolean lastLeftButtonPressed = false;
	private boolean leftButtonPressed = false;
	private boolean lastRightButtonPressed = false;
	private boolean rightButtonPressed = false;

	private short ticksSinceLastClick = -1;

	public InputHandler() {
		previousPos = new Vector2d(-10, -10);
		deltaMouseVector = new Vector2f(0, 0);
		deltaScrollVector = new Vector2i(0, 0);
	}

	public void init(Window window) {
		InputHandler.window = window.getID();
		glfwSetCursorPosCallback(window.getID(), (windowHandle, xPos, yPos) -> {
			window.cursorX = xPos;
			window.cursorY = yPos;
			if (previousPos.x != -10 && previousPos.y != -10 && focused) {
				deltaMouseVector.x = (float)(xPos - previousPos.x);
				deltaMouseVector.y = (float)(yPos - previousPos.y);
			}
			previousPos.x = xPos;
			previousPos.y = yPos;
		});
		glfwSetCursorEnterCallback(window.getID(), (windowHandle, entered) -> focused = entered);
		glfwSetMouseButtonCallback(window.getID(), (windowHandle, button, action, mode) -> {
			leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
			rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
		});
		glfwSetScrollCallback(window.getID(), (window1, xoffset, yoffset) -> {
			deltaScrollVector.x += (float)xoffset;
			deltaScrollVector.y += (float)yoffset;
		});
		glfwSetKeyCallback(window.getID(), (win, key, scancode, action, mods) -> processInput(new KeyBind(key, scancode, action, mods)));
	}

	public abstract void processInput(KeyBind keyBind);

	public void resetDeltas() {
		deltaMouseVector.zero();
		deltaScrollVector.zero();
	}

	public Vector2f getDeltaMouseVector(float sensitivity) {
		return deltaMouseVector.mul(sensitivity);
	}

	public float getMouseDeltaX() {
		return deltaMouseVector.x();
	}

	public float getMouseDeltaY() {
		return deltaMouseVector.y();
	}

	public void update() {
		updateMouseButtons();
	}

	public void updateMouseButtons() {
		if (lastLeftButtonPressed && !leftButtonPressed) {
			ticksSinceLastClick = -1;
		}
		lastLeftButtonPressed = leftButtonPressed;
		lastRightButtonPressed = rightButtonPressed;
		//Make sure this doesnt explode
		if (ticksSinceLastClick >= Short.MAX_VALUE) {
			ticksSinceLastClick = -2;
		}
		//If there was not a recent click dont increment
		if (ticksSinceLastClick >= -1) {
			ticksSinceLastClick++;
		}
	}

	public short getTicksSinceLastClick() {
		return ticksSinceLastClick;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}

	public boolean isRightButtonReleased() {
		return !rightButtonPressed && lastRightButtonPressed;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public boolean isLeftButtonReleased() {
		return !leftButtonPressed && lastLeftButtonPressed;
	}

	public void setFocus(boolean focused) {
		this.focused = focused;
	}

	public boolean isFocused() {
		return focused;
	}

	public static long getWindow() {
		return window;
	}

	public int getDeltaScrollVertical() {
		return deltaScrollVector.y;
	}

	public int getDeltaScrollHorizontal() {
		return deltaScrollVector.x;
	}
}
