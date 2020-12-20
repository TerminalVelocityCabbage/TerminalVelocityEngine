package com.terminalvelocitycabbage.engine.client.state;

public class State {

	public final String state;
	public String details;

	public State(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public String getDetails() {
		return details;
	}

	public State setDetails(String details) {
		this.details = details;
		return this;
	}
}
