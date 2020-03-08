package engine.client;

import com.github.simplenet.Client;
import org.fusesource.jansi.AnsiConsole;

public abstract class ClientBase {

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
		// Attempt to connect to a server AFTER registering listeners.
		client.connect("localhost", 49056);
	}

	public Client getClient() {
		return client;
	}

	public String getID() {
		return id;
	}
}
