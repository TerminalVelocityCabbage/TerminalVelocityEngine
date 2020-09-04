package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ClientStartEvent extends Event {

	public static final String PRE_INIT = "clientStartEventPreInit";
	public static final String INIT = "clientStartEventInit";
	public static final String POST_INIT = "clientStartEventPostInit";
	public static final String START = "clientStartEventStart";

	Client client;

	public ClientStartEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}
