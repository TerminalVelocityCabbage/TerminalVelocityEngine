package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ClientStartEvent extends Event {

	public static final String PRE_INIT = "clientPreInit";
	public static final String INIT = "clientInit";
	public static final String POST_INIT = "clientPostInit";
	public static final String START = "clientStart";

	Client client;

	public ClientStartEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}
