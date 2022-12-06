package com.terminalvelocitycabbage.templates.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class ServerChatEvent extends Event {

	public static final String RECEIVED = "serverChatEventReceived";

	private Client client;
	private String message;

	public ServerChatEvent(String name, Client client, String message) {
		super(EventContext.SERVER, name);
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
