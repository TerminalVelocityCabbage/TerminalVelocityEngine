package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class InputHandler {

	private static long window;

	private Vector2d previousPos;
	private final Vector2d currentPos;
	protected final Vector2f displayVector;

	private boolean focused = false;

	private boolean lastLeftButtonPressed = false;
	private boolean leftButtonPressed = false;
	private boolean lastRightButtonPressed = false;
	private boolean rightButtonPressed = false;

	private short ticksSinceLastClick = -1;

	public InputHandler() {
		previousPos = new Vector2d(0, 0);
		currentPos = new Vector2d(0, 0);
		displayVector = new Vector2f(0, 0);
	}

	public void init(Window window) {
		InputHandler.window = window.getID();
		glfwSetCursorPosCallback(window.getID(), (windowHandle, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
			mouseInput();
			previousPos.x = currentPos.x;
			previousPos.y = currentPos.y;
			window.cursorX = currentPos.x;
			window.cursorY = currentPos.y;
		});
		glfwSetCursorEnterCallback(window.getID(), (windowHandle, entered) -> focused = entered);
		glfwSetMouseButtonCallback(window.getID(), (windowHandle, button, action, mode) -> {
			leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
			rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
		});
		glfwSetKeyCallback(window.getID(), (win, key, scancode, action, mods) -> processInput(new KeyBind(key, scancode, action, mods)));
	}

	public abstract void processInput(KeyBind keyBind);


	public Vector2f getDisplayVector() {
		return displayVector;
	}

	public void mouseInput() {
		displayVector.x = 0;
		displayVector.y = 0;
		if (previousPos.x > 0 && previousPos.y > 0 && focused) {
			double deltaX = currentPos.x - previousPos.x;
			double deltaY = currentPos.y - previousPos.y;
			if (deltaX != 0.0) {
				displayVector.y = (float) deltaX;
			}
			if (deltaY != 0.0) {
				displayVector.x = (float) deltaY;
			}
		}
	}

	public void resetDisplayVector() {
		displayVector.x = 0;
		displayVector.y = 0;
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
}
