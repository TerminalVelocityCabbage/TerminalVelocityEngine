package com.terminalvelocitycabbage.templates.events.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.Event;
import com.terminalvelocitycabbage.engine.events.EventContext;

public class ServerLifecycleEvent extends Event {

	public static final String PRE_INIT = "serverLifecycleEventPreInit";
	public static final String INIT = "serverLifecycleEventInit";
	public static final String PRE_BIND = "serverLifecycleEventPreBind";
	public static final String STARTED = "serverLifecycleEventStart";
	public static final String STOPPING = "serverLifecycleEventStopping";
	public static final String STOPPED = "serverLifecycleEventStopped";

	Server server;

	public ServerLifecycleEvent(String name, Server server) {
		super(EventContext.SERVER, name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
