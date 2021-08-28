package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

public class CharInfo {

	private final int startX;

	private final int width;

	public CharInfo(int startX, int width) {
		this.startX = startX;
		this.width = width;
	}

	public int getStartX() {
		return startX;
	}

	public int getWidth() {
		return width;
	}
}
