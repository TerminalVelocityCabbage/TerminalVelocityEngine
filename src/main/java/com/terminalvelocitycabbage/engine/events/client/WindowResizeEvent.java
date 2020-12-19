package com.terminalvelocitycabbage.engine.events.client;

import com.terminalvelocitycabbage.engine.events.Event;

public class WindowResizeEvent extends Event {

	public static final String EVENT = "windowResizeEventEvent";

	public WindowResizeEvent(String name) {
		super(name);
	}

}
