package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ClientStartEvent extends Event {

	public static final String PRE_INIT = createID(ClientStartEvent.class, "pre");
	public static final String INIT = createID(ClientStartEvent.class, "init");
	public static final String POST_INIT = createID(ClientStartEvent.class, "post");
	public static final String START = createID(ClientStartEvent.class, "start");

	Client client;

	public ClientStartEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}
}
