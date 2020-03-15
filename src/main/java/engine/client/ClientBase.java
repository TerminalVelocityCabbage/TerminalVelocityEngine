package engine.client;

import com.github.simplenet.Client;
import engine.debug.Log;
import engine.events.EventDispatcher;
import engine.events.client.ClientConnectionEvent;
import org.fusesource.jansi.AnsiConsole;

public abstract class ClientBase extends EventDispatcher {

	String id;
	Client client;

	public ClientBase(String id) {
		this.id = id;
	}

	public void init() {
		preInit();
		//Enable Console colors
		AnsiConsole.systemInstall();

		//Start up the network listeners
		client = new Client();
	}

	private void preInit() {

	}

	public void start() {

		client.onConnect(() -> {
			Log.info(ClientConnectionEvent.CONNECT);
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.CONNECT, client));
		});

		client.preDisconnect(() -> {
			Log.info(ClientConnectionEvent.PRE_DISCONNECT);
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_DISCONNECT, client));
		});

		client.postDisconnect(() -> {
			Log.info(ClientConnectionEvent.POST_DISCONNECT);
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_DISCONNECT, client));
		});

		// Attempt to connect to a server AFTER registering listeners.
		client.connect("localhost", 49056);
	}

	public String getID() {
		return this.id;
	}
}
