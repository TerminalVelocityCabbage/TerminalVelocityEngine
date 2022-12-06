package com.terminalvelocitycabbage.templates.events.client;

import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class WindowResizeEvent extends Event {

	public static final String EVENT = "windowResizeEventEvent";

	public WindowResizeEvent(String name) {
		super(EventContext.CLIENT, name);
	}

}
