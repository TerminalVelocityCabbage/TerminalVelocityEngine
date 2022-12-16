package com.terminalvelocitycabbage.engine.client.input;

import com.terminalvelocitycabbage.engine.client.renderer.Window;
import com.terminalvelocitycabbage.engine.debug.Log;

import static org.lwjgl.glfw.GLFW.*;

public class InputListener {

	private long window;
	InputFrame currentFrame;

	public InputListener(Window window) {
		currentFrame = new InputFrame();
		init(window);
	}

	public void init(Window window) {
		this.window = window.getID();
		glfwSetCursorPosCallback(window.getID(), (windowHandle, xPos, yPos) -> {
			window.cursorX = xPos;
			window.cursorY = yPos;
			if (currentFrame.previousPos.x != -10 && currentFrame.previousPos.y != -10 && currentFrame.focused) {
				currentFrame.deltaMouseVector.x = (float)(xPos - currentFrame.previousPos.x);
				currentFrame.deltaMouseVector.y = (float)(yPos - currentFrame.previousPos.y);
			}
			currentFrame.previousPos.x = xPos;
			currentFrame.previousPos.y = yPos;
		});
		glfwSetCursorEnterCallback(window.getID(), (windowHandle, entered) -> currentFrame.inside = entered);
		glfwSetMouseButtonCallback(window.getID(), (windowHandle, button, action, mode) -> {
			currentFrame.leftButtonReleased = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE;
			currentFrame.leftButtonPressed = button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS;
			currentFrame.rightButtonReleased = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_RELEASE;
			currentFrame.rightButtonPressed = button == GLFW_MOUSE_BUTTON_RIGHT && action == GLFW_PRESS;
			currentFrame.focused = true;
		});
		glfwSetScrollCallback(window.getID(), (window1, xoffset, yoffset) -> {
			currentFrame.deltaScrollVector.x += (float)xoffset;
			currentFrame.deltaScrollVector.y += (float)yoffset;
		});
		glfwSetKeyCallback(window.getID(), (win, key, scancode, action, mods) -> {
			currentFrame.seenKeys.add(new KeyBind(win, key, scancode, action, mods));
		});
	}

	public void resetDeltas() {
		currentFrame.deltaMouseVector.zero();
		currentFrame.deltaScrollVector.zero();
		currentFrame.seenKeys.clear();
	}

	public void update() {
		updateMouseButtons();
	}

	public void updateMouseButtons() {
		if (currentFrame.leftButtonReleased) {
			currentFrame.ticksSinceLastClick = 0;
		}
		//Make sure this doesn't explode
		if (currentFrame.ticksSinceLastClick >= Short.MAX_VALUE - 1) {
			currentFrame.ticksSinceLastClick = Short.MAX_VALUE;
		}

		//Update left button stuff
		if (currentFrame.lastLeftButtonReleased) {
			currentFrame.leftButtonHeldTime = -1;
			currentFrame.lastLeftButtonReleased = false;
			currentFrame.leftButtonPressed = false;
		}
		if (currentFrame.leftButtonPressed && !currentFrame.leftButtonReleased) {
			currentFrame.leftButtonHeldTime++;
		}
		if (currentFrame.refLeftMouseClicked) {
			currentFrame.lastLeftButtonReleased = true;
			currentFrame.refLeftMouseClicked = false;
		}
		if (currentFrame.leftButtonReleased) {
			currentFrame.leftButtonPressed = false;
			currentFrame.refLeftMouseClicked = true;
			currentFrame.leftButtonReleased = false;
		}

		//Update right button stuff
		if (currentFrame.lastRightButtonReleased) {
			currentFrame.rightButtonHeldTime = -1;
			currentFrame.lastRightButtonReleased = false;
			currentFrame.rightButtonPressed = false;
		}
		if (currentFrame.rightButtonPressed && !currentFrame.rightButtonReleased) {
			currentFrame.rightButtonHeldTime++;
		}
		if (currentFrame.refRightMouseClicked) {
			currentFrame.lastRightButtonReleased = true;
			currentFrame.refRightMouseClicked = false;
		}
		if (currentFrame.rightButtonReleased) {
			currentFrame.rightButtonPressed = false;
			currentFrame.refRightMouseClicked = true;
			currentFrame.rightButtonReleased = false;
		}
	}

	public long getWindow() {
		return window;
	}

	public boolean isKeyPressed(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.keyCode()) == GLFW_PRESS;
	}

	public boolean isKeyReleased(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.keyCode()) == GLFW_RELEASE;
	}

	public boolean isKeyRepeated(KeyBind keyBind) {
		return glfwGetKey(window, keyBind.keyCode()) == GLFW_REPEAT;
	}

	public InputFrame getCurrentInputFrame() {
		return currentFrame;
	}
}
