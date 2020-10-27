package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ClientConnectionEvent extends Event {

	public static final String PRE_RECONNECT = "clientConnectionEventPreReConnect";
	public static final String RECONNECT_TRY_FAIL = "clientConnectionEventReConnectTryFail";
	public static final String POST_RECONNECT = "clientConnectionEventPostReConnect";
	public static final String RECONNECT_FAIL = "clientConnectionEventReConnectFailure";
	public static final String CONNECT = "clientConnectionEventConnect";
	public static final String PRE_DISCONNECT = "clientConnectionEventPreDisconnect";
	public static final String POST_DISCONNECT = "clientConnectionEventPostDisconnect";

	private Client client;

	public ClientConnectionEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
