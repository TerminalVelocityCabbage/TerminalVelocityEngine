package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerClientConnectionEvent extends Event {

	public static final String CONNECT = "serverConnect";
	public static final String PRE_DISCONNECT = "serverPreDisconnect";
	public static final String POST_DISCONNECT = "serverPostDisconnect";

	private Server server;
	private Client client;

	public ServerClientConnectionEvent(String name, Server server, Client client) {
		super(name);
		this.server = server;
		this.client = client;
	}

	public Server getServer() {
		return server;
	}

	public Client getClient() {
		return client;
	}
}
