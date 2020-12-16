package com.terminalvelocitycabbage.engine.client.renderer.ui;

public enum AnchorDirection {

	CENTER("center"),
	RIGHT("right"),
	LEFT("left"),
	UP("up"),
	DOWN("down"),
	RIGHT_DOWN("right_down"),
	RIGHT_UP("right_up"),
	LEFT_DOWN("left_down"),
	LEFT_UP("left_up");

	public String name;

	AnchorDirection(String name) {
		this.name = name;
	}
}
