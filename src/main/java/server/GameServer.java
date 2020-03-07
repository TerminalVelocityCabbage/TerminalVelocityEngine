package server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;
import engine.configs.Config;
import engine.debug.Log;
import engine.io.SaveManager;
import org.fusesource.jansi.AnsiConsole;

import java.util.concurrent.ConcurrentHashMap;

public class GameServer {

	String id = "default";

	public GameServer(String id) {
		this.id = id;
	}

	public void init() {
		Log.info("Server Starting with ID: " + id);
		if (!SaveManager.checkForSaveDirectory(id)) {
			Log.info("No save Directory found for this save, assuming its a new save.");
			if (SaveManager.createSaveDirectory(id)) {
				Config config = Config.builder()
						.setFileName("config")
						.addKey("ip", "localhost")
						.addKey("port", "49056")
						.addKey("motd", "Message of the Day!")
						.build();
				if (SaveManager.createConfigFile(config, id)) {
					start();
					return;
				}
			}
		}
		Log.error("Something went wrong during server initialization, the server will not start.");
	}

	public void start() {
		//Enable Console colors
		AnsiConsole.systemInstall();

		//Start up the network listeners
		var server = new Server();

		//Create a map of the clients and their usernames
		var clients = new ConcurrentHashMap<Client, String>();

		server.onConnect(client -> {

			Log.info("Client attempting to establish connection.");

			//When a client disconnects remove them from the map
			client.postDisconnect(() -> {
				Log.info(clients.get(client) + " left.");
				clients.remove(client);
			});

			//Read bytes as they come in one at a time
			client.readByteAlways(opcode -> {
				switch (opcode) {
					case 1: //Change nickname
						client.readString(username -> {
							clients.put(client, username);
							Log.info("Client packet received: " + clients.get(client) + " successfully joined.");
						});
						break;
					case 2: //Send message to connected clients
						client.readString(message -> {
							message = clients.get(client) + ": " + message;
							server.queueAndFlushToAllExcept(Packet.builder().putString(message), client);
						});
						break;
				}
			});
		});

		//Bind the server to the configured port and IP
		//TODO read from server config
		server.bind("localhost", 49056);
	}

}
