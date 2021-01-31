package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class ServerClientPacketReceivedEvent extends Event {

	public static final String RECEIVED = "serverClientPacketReceivedEventReceived";

	//TODO this will need to house more information about the joined player like uuid etc.
	private Client client;
	private String username;

	public ServerClientPacketReceivedEvent(String name, Client client, String username) {
		super(EventContext.SERVER, name);
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
