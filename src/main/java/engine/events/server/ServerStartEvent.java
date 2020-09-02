package engine.events.server;

import com.github.simplenet.Server;
import engine.events.Event;

public class ServerStartEvent extends Event {

	public static final String PRE_INIT = "serverPreInit";
	public static final String INIT = "serverInit";
	public static final String POST_INIT = "serverPostInit";
	public static final String START = "serverStart";

	Server server;

	public ServerStartEvent(String name, Server server) {
		super(name);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}
}
