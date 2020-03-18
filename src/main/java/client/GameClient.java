package client;

import com.github.simplenet.packet.Packet;
import engine.client.ClientBase;
import engine.client.PingClient;
import engine.debug.Log;
import engine.events.HandleEvent;
import engine.events.client.ClientConnectionEvent;
import engine.events.client.ClientStartEvent;
import engine.server.PacketTypes;

import java.util.Scanner;

public class GameClient extends ClientBase {

	public static final String ADDRESS = "localhost";
	public static final int PORT = 49056;

	public GameClient(String id) {
		super(id);
		addEventHandler(this);
	}

	@HandleEvent(ClientStartEvent.START)
	public void onStart(ClientStartEvent event) {
		try {
			var pingClient = new PingClient(ADDRESS, PORT);
			if (pingClient.ping().getResult()) {
				connect(ADDRESS, PORT);
			} else {
				Log.error("Server not found.");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@HandleEvent({ClientConnectionEvent.CONNECT})
	public void onConnect(ClientConnectionEvent event) {
		var scanner = new Scanner(System.in);

		// If messages arrive from other clients, print them to the console.
		event.getClient().readByteAlways(opcode -> {
			switch (opcode) {
				case PacketTypes.CHAT:
					event.getClient().readString(System.out::println);
			}
		});

		//Send a username packet to the server with the id
		Packet.builder().putByte(PacketTypes.CLIENT_VALIDATION).putString(getID()).queueAndFlush(event.getClient());

		//Accept user-input for the chat server.
		while (!disconnected()) {
			var message = scanner.nextLine();
			Packet.builder().putByte(PacketTypes.CHAT).putString(message).queueAndFlush(event.getClient());
		}
	}

	@HandleEvent({ClientConnectionEvent.POST_DISCONNECT})
	public void onDisconnect(ClientConnectionEvent event) {
		if (!disconnected()) {
			try {
				Log.warn("Lost connection to the server.");
				reconnect(ADDRESS, PORT, 10, 3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@HandleEvent({ClientConnectionEvent.PRE_RECONNECT})
	public void preReconnect(ClientConnectionEvent event) {
		Log.info("Attempting to Reconnect...");
	}

	@HandleEvent({ClientConnectionEvent.RECONNECT_TRY_FAIL})
	public void reconnectTryFail(ClientConnectionEvent event) {
		Log.info("Attempt failed...");
	}

	@HandleEvent({ClientConnectionEvent.POST_RECONNECT})
	public void postReconnect(ClientConnectionEvent event) {
		Log.info("Reconnected.");
	}

	@HandleEvent({ClientConnectionEvent.RECONNECT_FAIL})
	public void reconnectFail(ClientConnectionEvent event) {
		Log.error("Reconnect failure.");
	}
}
