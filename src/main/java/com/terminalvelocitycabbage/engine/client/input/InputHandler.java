package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;

public abstract class InputHandler {

	private static long window;

	private final Vector2d previousPos;
	protected final Vector2f deltaMouseVector;
	protected final Vector2i deltaScrollVector;

	private boolean focused = false;

	//Temp Vars for left mouse button
	private boolean lastLeftButtonReleased = false;
	private boolean leftButtonReleased = false;
	private boolean lastLeftButtonPressed = false;
	private boolean leftButtonPressed = false;
	//Ref Vars for left mouse button
	private boolean refLeftMouseClicked;
	private short leftButtonHeldTime = -1;

	//Temp Vars for right mouse button
	private boolean lastRightButtonReleased = false;
	private boolean rightButtonReleased = false;
	private boolean lastRightButtonPressed = false;
	private boolean rightButtonPressed = false;
	//Ref Vars for right mouse button
	private boolean refRightMouseClicked;
	private short rightButtonHeldTime = -1;

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
			leftButtonReleased = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE;
			leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
			rightButtonReleased = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE;
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

	public Vector2d getMousePos() {
		return previousPos;
	}

	public float getMouseX() {
		return (float) previousPos.x;
	}

	public float getMouseY() {
		return (float) previousPos.y;
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
		if (leftButtonReleased) {
			ticksSinceLastClick = 0;
		}
		//Make sure this doesn't explode
		if (ticksSinceLastClick >= Short.MAX_VALUE - 1) {
			ticksSinceLastClick = Short.MAX_VALUE;
		}

		//Update left button stuff
		if (lastLeftButtonReleased) {
			leftButtonHeldTime = -1;
			lastLeftButtonReleased = false;
			leftButtonPressed = false;
		}
		if (leftButtonPressed && !leftButtonReleased) {
			leftButtonHeldTime++;
		}
		if (refLeftMouseClicked) {
			lastLeftButtonReleased = true;
			refLeftMouseClicked = false;
		}
		if (leftButtonReleased) {
			leftButtonPressed = false;
			refLeftMouseClicked = true;
			leftButtonReleased = false;
		}

		//Update right button stuff
		if (lastRightButtonReleased) {
			rightButtonHeldTime = -1;
			lastRightButtonReleased = false;
			rightButtonPressed = false;
		}
		if (rightButtonPressed && !rightButtonReleased) {
			rightButtonHeldTime++;
		}
		if (refRightMouseClicked) {
			lastRightButtonReleased = true;
			refRightMouseClicked = false;
		}
		if (rightButtonReleased) {
			rightButtonPressed = false;
			refRightMouseClicked = true;
			rightButtonReleased = false;
		}
	}

	public short getTicksSinceLastClick() {
		return ticksSinceLastClick;
	}

	public boolean isLeftButtonClicked() {
		return refLeftMouseClicked;
	}

	public boolean isLeftButtonHeldFor(short time) {
		return time >= leftButtonHeldTime;
	}

	public boolean isLeftButtonHolding() {
		return leftButtonPressed;
	}

	public boolean isRightButtonClicked() {
		return refRightMouseClicked;
	}

	public boolean isRightButtonHeldFor(short time) {
		return time >= rightButtonHeldTime;
	}

	public boolean isRightButtonHolding() {
		return rightButtonPressed;
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
