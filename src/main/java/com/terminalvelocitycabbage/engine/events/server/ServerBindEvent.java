package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ServerBindEvent extends Event {

	public static final EventIdentifier PRE = new EventIdentifier(ServerBindEvent.class, "pre");
	public static final EventIdentifier POST = new EventIdentifier(ServerBindEvent.class, "post");

	private Server server;

	public ServerBindEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
