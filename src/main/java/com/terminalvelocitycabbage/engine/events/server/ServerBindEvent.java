package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerBindEvent extends Event {

	public static final String PRE = createID(ServerBindEvent.class, "pre");
	public static final String POST = createID(ServerBindEvent.class, "post");

	private Server server;

	public ServerBindEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
