package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerCommandReceivedEvent extends Event {

	public static final String RECEIVED = "serverCommandReceivedEventReceived";

	private Client client;
	private String command;

	public ServerCommandReceivedEvent(String name, Client client, String command) {
		super(name);
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
