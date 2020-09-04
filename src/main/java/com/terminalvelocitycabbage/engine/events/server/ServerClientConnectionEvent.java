package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ServerClientConnectionEvent extends Event {

	public static final EventIdentifier CONNECT = new EventIdentifier(ServerClientConnectionEvent.class, "connect");
	public static final EventIdentifier PRE_DISCONNECT = new EventIdentifier(ServerClientConnectionEvent.class, "pre");
	public static final EventIdentifier POST_DISCONNECT = new EventIdentifier(ServerClientConnectionEvent.class, "post");

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
