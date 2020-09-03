package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;

public class ServerStartEvent extends Event {

	public static final String PRE_INIT = createID(ServerStartEvent.class, "pre");
	public static final String INIT = createID(ServerStartEvent.class, "init");
	public static final String POST_INIT = createID(ServerStartEvent.class, "post");
	public static final String START = createID(ServerStartEvent.class, "start");

	Server server;

	public ServerStartEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
