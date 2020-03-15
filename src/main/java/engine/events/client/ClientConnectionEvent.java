package engine.events.client;

import com.github.simplenet.Client;
import engine.events.Event;

public class ClientConnectionEvent extends Event {

	public static final String CONNECT = "clientConnect";
	public static final String PRE_DISCONNECT = "clientPreDisconnect";
	public static final String POST_DISCONNECT = "clientPostDisconnect";

	private Client client;

	public ClientConnectionEvent(String name, Client client) {
		super(name);
		this.client = client;
	}

	public Client getClient() {
		return client;
	}

}
