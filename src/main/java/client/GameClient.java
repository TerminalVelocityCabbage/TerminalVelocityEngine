package client;

import com.github.simplenet.packet.Packet;
import engine.client.ClientBase;
import engine.events.HandleEvent;
import engine.events.client.ClientConnectionEvent;

import java.util.Scanner;

public class GameClient extends ClientBase {

	public GameClient(String id) {
		super(id);
	}

	public void init() {
		super.init();
		//Register this class as an event listener
		super.addEventHandler(this);
		this.start();
	}

	@Override
	public void start() {
		super.start();
	}

	@HandleEvent({ClientConnectionEvent.CONNECT})
	public void onConnect(ClientConnectionEvent event) {
		var scanner = new Scanner(System.in);

		// If messages arrive from other clients, print them to the console.
		event.getClient().readStringAlways(System.out::println);

		//Send a username packet to the server with the id
		Packet.builder().putByte(1).putString(getID()).queueAndFlush(event.getClient());

		// Infinite loop to accept user-input for the chat server.
		while (true) {
			// Read the client's message from the console.
			var message = scanner.nextLine();

			// If this client types a command use opcode 0
			// Otherwise, send a packet to the server containing the client's message use opcode 2.
			if (message.startsWith("/")) {
				Packet.builder().putByte(0).putString(message).queueAndFlush(event.getClient());
			} else {
				Packet.builder().putByte(2).putString(message).queueAndFlush(event.getClient());
			}
		}
	}
}
