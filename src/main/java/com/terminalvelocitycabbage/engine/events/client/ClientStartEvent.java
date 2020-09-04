package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ClientStartEvent extends Event {

	public static final EventIdentifier PRE_INIT = new EventIdentifier(ClientStartEvent.class, "pre");
	public static final EventIdentifier INIT = new EventIdentifier(ClientStartEvent.class, "init");
	public static final EventIdentifier POST_INIT = new EventIdentifier(ClientStartEvent.class, "post");
	public static final EventIdentifier START = new EventIdentifier(ClientStartEvent.class, "start");

	Client client;

	public ClientStartEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}
