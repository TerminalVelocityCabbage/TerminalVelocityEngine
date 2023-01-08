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

	public boolean toggleState(String name) {
		return states.get(name).toggle();
	}

	public void enable(String name) {
		states.get(name).enable();
	}

	public void disable(String name) {
		states.get(name).disable();
	}

	public boolean isStateActive(String name) {
		if (!states.containsKey(name)) Log.crash("State not found " + name, new RuntimeException("no state of name " + name + " registered in this state handler"));
		return states.get(name).enabled();
	}

	public boolean isStateActive(State state) {
		return isStateActive(state.getName());
	}

	private boolean wasStateActiveLastTick(String name) {
		if (!states.containsKey(name)) Log.crash("State not found " + name, new RuntimeException("no state of name " + name + " registered in this state handler"));
		return states.get(name).wasEnabledLastTick();
	}

	public boolean wasStateActiveLastTick(State state) {
		return wasStateActiveLastTick(state.getName());
	}

	public void tick() {
		states.forEach((s, state) -> state.tick());
	}
}
