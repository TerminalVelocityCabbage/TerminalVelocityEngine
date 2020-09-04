package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ClientConnectionEvent extends Event {

	public static final EventIdentifier PRE_RECONNECT = new EventIdentifier(ClientConnectionEvent.class, "preReconnect");
	public static final EventIdentifier RECONNECT_TRY_FAIL = new EventIdentifier(ClientConnectionEvent.class, "reconnectTryFail");
	public static final EventIdentifier POST_RECONNECT = new EventIdentifier(ClientConnectionEvent.class, "postReconnect");
	public static final EventIdentifier RECONNECT_FAIL = new EventIdentifier(ClientConnectionEvent.class, "reconnectFailure");
	public static final EventIdentifier CONNECT = new EventIdentifier(ClientConnectionEvent.class, "connect");
	public static final EventIdentifier PRE_DISCONNECT = new EventIdentifier(ClientConnectionEvent.class, "preDisconnect");
	public static final EventIdentifier POST_DISCONNECT = new EventIdentifier(ClientConnectionEvent.class, "postDisconnect");

	private Client client;

	public ClientConnectionEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
