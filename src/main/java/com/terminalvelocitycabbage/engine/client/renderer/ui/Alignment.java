package com.terminalvelocitycabbage.engine.client.renderer.ui;

public abstract class Alignment {

	public enum Horizontal {

		LEFT(-1, "left"),
		CENTER(0, "center"),
		RIGHT(1, "right");

		int start;
		String name;

		Horizontal(int start, String name) {
			this.start = start;
			this.name = name;
		}
	}

	public enum Vertical {

		TOP(1, "top"),
		CENTER(0, "center"),
		BOTTOM(-1, "bottom");

		int start;
		String name;

		Vertical(int start, String name) {
			this.start = start;
			this.name = name;
		}
	}
}
