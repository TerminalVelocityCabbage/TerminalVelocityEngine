package client;

import com.github.simplenet.packet.Packet;
import engine.client.ClientBase;

import java.util.Scanner;

public class GameClient extends ClientBase {

	public GameClient(String id) {
		super(id);
	}

	public void init() {
		super.init();

		// This callback is invoked when this client connects to a server.
		getClient().onConnect(() -> {
			var scanner = new Scanner(System.in);

			// If messages arrive from other clients, print them to the console.
			getClient().readStringAlways(System.out::println);

			Packet.builder().putByte(1).putString(getID()).queueAndFlush(getClient());

			// Infinite loop to accept user-input for the chat server.
			while (true) {
				// Read the client's message from the console.
				var message = scanner.nextLine();

				// If this client types "/leave", close their connection to the server.
				if ("/leave".equals(message)) {
					getClient().close();
					break;
				}

				// Otherwise, send a packet to the server containing the client's message.
				Packet.builder().putByte(2).putString(message).queueAndFlush(getClient());
			}
		});

		this.start();
	}

	@Override
	public void start() {
		super.start();
	}
}
