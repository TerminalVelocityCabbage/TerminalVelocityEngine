package com.terminalvelocitycabbage.templates.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class ServerCommandReceivedEvent extends Event {

	public static final String RECEIVED = "serverCommandReceivedEventReceived";

	private Client client;
	private String command;

	public ServerCommandReceivedEvent(String name, Client client, String command) {
		super(EventContext.SERVER, name);
		this.client = client;
		this.command = command;
	}

	public Client getClient() {
		return client;
	}

	public String getCommand() {
		return command;
	}
}
