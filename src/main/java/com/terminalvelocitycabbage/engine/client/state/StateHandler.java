package com.terminalvelocitycabbage.engine.client.state;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

public class StateHandler {

	public static State DEFAULT_STATE = new State("default", "The default state.");

	public Map<String, State> states;

	public StateHandler() {
		this.states = new HashMap<>();
		addState(DEFAULT_STATE, true);
	}

	public void addState(State state) {
		addState(state, false);
	}

	public void addState(State state, boolean enable) {
		states.put(state.getName(), state.setEnabled(enable));
	}

	public void toggleState(String name) {
		states.get(name).toggle();
	}

	public boolean isStateActive(String name) {
		if (!states.containsKey(name)) Log.crash("State not found " + name, new RuntimeException("no state of name " + name + " registered in this state handler"));
		return states.get(name).enabled();
	}
}
