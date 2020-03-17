package client;

import com.github.simplenet.packet.Packet;
import engine.client.ClientBase;
import engine.events.HandleEvent;
import engine.events.client.ClientConnectionEvent;
import engine.events.client.ClientStartEvent;
import engine.server.PacketTypes;

import java.util.Scanner;

public class GameClient extends ClientBase {

	public GameClient(String id) {
		super(id);
		addEventHandler(this);
	}

	@HandleEvent(ClientStartEvent.START)
	public void onStart(ClientStartEvent event) {
		connect("localhost", 49056);
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
		while (true) {
			var message = scanner.nextLine();
			Packet.builder().putByte(PacketTypes.CHAT).putString(message).queueAndFlush(event.getClient());
		}
	}
}
