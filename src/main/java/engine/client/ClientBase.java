package engine.client;

import com.github.simplenet.Client;
import engine.events.EventDispatcher;
import engine.events.client.ClientConnectionEvent;
import engine.events.client.ClientStartEvent;
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
		dispatchEvent(new ClientStartEvent(ClientStartEvent.INIT, client));
		postInit();
	}

	private void preInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.PRE_INIT, client));
	}

	private void postInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.POST_INIT, client));
	}

	public void start() {

		client.onConnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.CONNECT, client));
		});

		client.preDisconnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_DISCONNECT, client));
		});

		client.postDisconnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_DISCONNECT, client));
		});

		dispatchEvent(new ClientStartEvent(ClientStartEvent.START, client));
	}

	public void connect(String address, int port) {
		client.connect(address, port);
	}

	public String getID() {
		return this.id;
	}
}
