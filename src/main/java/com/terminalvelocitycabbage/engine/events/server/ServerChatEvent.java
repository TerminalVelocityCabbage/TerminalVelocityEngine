package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerChatEvent extends Event {

	public static final String RECEIVED = createID(ServerChatEvent.class, "received");

	private Client client;
	private String message;

	public ServerChatEvent(String name, Client client, String message) {
		super(name);
		this.client = client;
		this.message = message;
	}

	public Client getClient() {
		return client;
	}

	public String getMessage() {
		return message;
	}
}
