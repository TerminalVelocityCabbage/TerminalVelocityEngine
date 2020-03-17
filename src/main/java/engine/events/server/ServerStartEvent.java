package engine.events.server;

import com.github.simplenet.Server;
import engine.events.Event;

public class ServerStartEvent extends Event {

	public static final String PRE_INIT = "preInit";
	public static final String INIT = "init";
	public static final String POST_INIT = "postInit";
	public static final String START = "start";

	Server server;

	public ServerStartEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
