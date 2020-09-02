package com.terminalvelocitycabbage.engine.events.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ClientConnectionEvent extends Event {

	public static final String PRE_RECONNECT = "clientPreReConnect";
	public static final String RECONNECT_TRY_FAIL = "clientReConnectTryFail";
	public static final String POST_RECONNECT = "clientPostReConnect";
	public static final String RECONNECT_FAIL = "clientReConnectFailure";
	public static final String CONNECT = "clientConnect";
	public static final String PRE_DISCONNECT = "clientPreDisconnect";
	public static final String POST_DISCONNECT = "clientPostDisconnect";

	private Client client;

	public ClientConnectionEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
