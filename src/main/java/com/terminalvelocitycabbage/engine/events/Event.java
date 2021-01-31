package com.terminalvelocitycabbage.engine.events;

public abstract class Event {

	private final EventContext context;
	private final String name;

	public Event(EventContext context, String name){
		this.context = context;
		this.name = name;
	}

	public EventContext getContext() {
		return context;
	}

	public String getName() {
		return name;
	}
}
