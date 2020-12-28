package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

public enum AnchorDirection {

	CENTER("center", 0, 0),
	RIGHT("right", 1, 0),
	LEFT("left", -1, 0),
	UP("up", 0, 1),
	DOWN("down", 0, -1),
	RIGHT_DOWN("right_down", 1, -1),
	RIGHT_UP("right_up", 1, 1),
	LEFT_DOWN("left_down", -1, -1),
	LEFT_UP("left_up", -1, 1);

	public String name;
	public int xDirection;
	public int yDirection;

	AnchorDirection(String name, int xDirection, int yDirection) {
		this.name = name;
		this.xDirection = xDirection;
		this.yDirection = yDirection;
	}
}
