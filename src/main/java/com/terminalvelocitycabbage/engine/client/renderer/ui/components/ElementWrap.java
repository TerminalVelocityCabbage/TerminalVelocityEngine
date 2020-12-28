package com.terminalvelocitycabbage.engine.client.renderer.ui.components;

public enum ElementWrap {

	WRAP(true, "wrap"),
	NO_WRAP(false, "no_wrap");

	public boolean wrap;
	public String name;

	ElementWrap(boolean wrap, String name) {
		this.wrap = wrap;
		this.name = name;
	}

	public boolean isWrap() {
		return wrap;
	}

	public String getName() {
		return name;
	}
}
