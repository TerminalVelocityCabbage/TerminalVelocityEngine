package com.terminalvelocitycabbage.engine.client.state;

import java.util.HashMap;
import java.util.Map;

public class StateHandler {

	public static State DEFAULT_STATE = new State("default").setDetails("The default state.");

	public Map<String, State> states;
	public State activeState;

	public StateHandler() {
		this.states = new HashMap<>();
		this.activeState = DEFAULT_STATE;
	}

	public void addState(State state) {
		states.put(state.getState(), state);
	}

	public void setState(String state) {
		if (states.containsKey(state)) {
			activeState = states.get(state);
		} else {
			throw new RuntimeException("No state defined by " + state);
		}
	}

	public void resetState() {
		this.activeState = DEFAULT_STATE;
	}

	public boolean isActive(String state) {
		return activeState.state.equals(state);
	}

}
