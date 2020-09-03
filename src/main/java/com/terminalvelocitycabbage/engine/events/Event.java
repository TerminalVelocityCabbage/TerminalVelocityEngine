package com.terminalvelocitycabbage.engine.events;

public abstract class Event {
	private String name;

	public Event(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getID() {
		return this.getClass().getPackageName() + "." + this.getClass().getName();
	}

	public static String createID(Class<? extends Event> event, String typeName) {
		return event.getPackageName() + "." + event.getName() + "." + typeName;
	}
}
