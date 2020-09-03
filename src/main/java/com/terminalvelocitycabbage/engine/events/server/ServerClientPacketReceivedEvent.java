package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerClientPacketReceivedEvent extends Event {

	public static final String RECEIVED = createID(ServerClientPacketReceivedEvent.class, "received");

	//TODO this will need to house more information about the joined player like uuid etc.
	private Client client;
	private String username;

	public ServerClientPacketReceivedEvent(String name, Client client, String username) {
		super(name);
		this.client = client;
		this.username = username;
	}

	public Client getClient() {
		return client;
	}

	public String getUsername() {
		return username;
	}
}
