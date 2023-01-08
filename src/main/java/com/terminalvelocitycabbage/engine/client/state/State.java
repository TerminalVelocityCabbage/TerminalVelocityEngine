package com.terminalvelocitycabbage.engine.client.state;

public class State {

	final String name;
	String description;
	boolean enabled;
	boolean wasEnabledLastTick;

	public State(String name, String description) {
		this.name = name;
		this.description = description;
		this.enabled = false;
		this.wasEnabledLastTick = false;
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

	public boolean toggle() {
		enabled = !enabled;
		return enabled;
	}

	public boolean enabled() {
		return enabled;
	}

    public void enable() {
		enabled = true;
    }

	public void disable() {
		enabled = false;
	}

	public void tick() {
		wasEnabledLastTick = enabled;
	}

	public boolean wasEnabledLastTick() {
		return wasEnabledLastTick;
	}
}
