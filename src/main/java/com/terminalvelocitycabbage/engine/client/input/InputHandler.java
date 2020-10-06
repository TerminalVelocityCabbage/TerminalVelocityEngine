package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public abstract class InputHandler {

	private Vector2d previousPos;
	private final Vector2d currentPos;
	private final Vector2f displayVector;

	private boolean focused = false;

	private boolean leftButtonPressed = false;
	private boolean rightButtonPressed = false;

	public InputHandler() {
		previousPos = new Vector2d(0, 0);
		currentPos = new Vector2d(0, 0);
		displayVector = new Vector2f(0, 0);
	}

	public void init(Window window) {
		glfwSetCursorPosCallback(window.getID(), (windowHandle, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
			mouseInput(window);
			previousPos.x = currentPos.x;
			previousPos.y = currentPos.y;
		});
		glfwSetCursorEnterCallback(window.getID(), (windowHandle, entered) -> {
			focused = entered;
		});
		glfwSetMouseButtonCallback(window.getID(), (windowHandle, button, action, mode) -> {
			leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		});
	}

	public abstract void processInput(Window window);


	public Vector2f getDisplayVector() {
		return displayVector;
	}

	public void mouseInput(Window window) {
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

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public void setFocus(boolean focused) {
		this.focused = focused;
	}

	public boolean isFocused() {
		return focused;
	}
}
