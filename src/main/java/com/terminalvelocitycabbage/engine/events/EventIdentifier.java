package com.terminalvelocitycabbage.engine.events;

public class EventIdentifier {

	String identifier;

	public EventIdentifier(Class<? extends Event> event, String typeName) {
		this.identifier = event.getPackageName() + "." + event.getName() + "." + typeName;
	}

	public String getIdentifier() {
		return identifier;
	}
}
