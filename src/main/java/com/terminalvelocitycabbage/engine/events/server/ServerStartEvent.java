package com.terminalvelocitycabbage.engine.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventIdentifier;

public class ServerStartEvent extends Event {

	public static final String PRE_INIT = "serverStartEventPreInit";
	public static final String INIT = "serverStartEventInit";
	public static final String POST_INIT = "serverStartEventPostInit";
	public static final String START = "serverStartEventStart";

	Server server;

	public ServerStartEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
