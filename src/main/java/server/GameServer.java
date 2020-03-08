package server;

import com.github.simplenet.Client;
import com.github.simplenet.packet.Packet;
import engine.debug.Log;
import engine.server.ServerBase;

import java.util.concurrent.ConcurrentHashMap;

public class GameServer extends ServerBase {

	public GameServer(String id) {
		super(id);
	}

	@Override
	public void init() {
		super.init();
		//Create a map of the clients and their usernames
		var clients = new ConcurrentHashMap<Client, String>();

		getServer().onConnect(client -> {

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
							getServer().queueAndFlushToAllExcept(Packet.builder().putString(message), client);
						});
						break;
				}
			});
		});
		this.start();
	}

	@Override
	public void start() {
		super.start();
	}
}
