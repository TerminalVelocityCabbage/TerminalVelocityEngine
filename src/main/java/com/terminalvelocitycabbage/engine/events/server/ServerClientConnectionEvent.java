package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerClientConnectionEvent extends Event {

	public static final String CONNECT = createID(ServerClientConnectionEvent.class, "connect");
	public static final String PRE_DISCONNECT = createID(ServerClientConnectionEvent.class, "pre");
	public static final String POST_DISCONNECT = createID(ServerClientConnectionEvent.class, "post");

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
