package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ClientConnectionEvent extends Event {

	public static final String PRE_RECONNECT = createID(ClientConnectionEvent.class, "preReconnect");
	public static final String RECONNECT_TRY_FAIL = createID(ClientConnectionEvent.class, "reconnectTryFail");
	public static final String POST_RECONNECT = createID(ClientConnectionEvent.class, "postReconnect");
	public static final String RECONNECT_FAIL = createID(ClientConnectionEvent.class, "reconnectFailure");
	public static final String CONNECT = createID(ClientConnectionEvent.class, "connect");
	public static final String PRE_DISCONNECT = createID(ClientConnectionEvent.class, "preDisconnect");
	public static final String POST_DISCONNECT = createID(ClientConnectionEvent.class, "postDisconnect");

	private Client client;

	public ClientConnectionEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
