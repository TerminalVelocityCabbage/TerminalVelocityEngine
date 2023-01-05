package com.terminalvelocitycabbage.engine.client.state;

public class State {

	final String name;
	String description;
	boolean enabled;

	public State(String name, String description) {
		this.name = name;
		this.description = description;
		this.enabled = false;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public State setEnabled(boolean enable) {
		enabled = enable;
		return this;
	}

	public void toggle() {
		enabled = !enabled;
	}

	public boolean enabled() {
		return enabled;
	}
}
