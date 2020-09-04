package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ServerStartEvent extends Event {

	public static final EventIdentifier PRE_INIT = new EventIdentifier(ServerStartEvent.class, "pre");
	public static final EventIdentifier INIT = new EventIdentifier(ServerStartEvent.class, "init");
	public static final EventIdentifier POST_INIT = new EventIdentifier(ServerStartEvent.class, "post");
	public static final EventIdentifier START = new EventIdentifier(ServerStartEvent.class, "start");

	Server server;

	public ServerStartEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
