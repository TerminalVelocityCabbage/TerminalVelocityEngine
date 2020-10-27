package com.terminalvelocitycabbage.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.events.Event;

public class ServerChatEvent extends Event {

	public static final String RECEIVED = "serverChatEventReceived";

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
