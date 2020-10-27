package com.terminalvelocitycabbage.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.events.Event;

public class ServerBindEvent extends Event {

	public static final String PRE = "serverBindEventPre";
	public static final String POST = "serverBindEventPost";

	private Server server;

	public ServerBindEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
