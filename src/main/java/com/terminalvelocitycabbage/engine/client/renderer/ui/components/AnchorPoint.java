package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

public enum AnchorPoint {

	TOP_LEFT("top_left", -1, 1, AnchorDirection.RIGHT_DOWN),
	TOP_MIDDLE("top_middle", 0, 1, AnchorDirection.DOWN),
	TOP_RIGHT("top_right", 1, 1, AnchorDirection.LEFT_DOWN),
	LEFT_MIDDLE("left_middle", -1, 0, AnchorDirection.RIGHT),
	MIDDLE_MIDDLE("middle_middle", 0, 0, AnchorDirection.CENTER),
	RIGHT_MIDDLE("right_middle", 1, 0, AnchorDirection.LEFT),
	BOTTOM_LEFT("bottom_left", -1, -1, AnchorDirection.RIGHT_UP),
	BOTTOM_MIDDLE("bottom_middle", 0, -1, AnchorDirection.UP),
	BOTTOM_RIGHT("bottom_right", 1, -1, AnchorDirection.LEFT_UP);

	public String name;
	public float xPos;
	public float yPos;
	public AnchorDirection defaultAnchorDirection;

	AnchorPoint(String name, float xPos, float yPos, AnchorDirection defaultAnchorDirection) {
		this.name = name;
		this.xPos = xPos;
		this.yPos = yPos;
		this.defaultAnchorDirection = defaultAnchorDirection;
	}
}
