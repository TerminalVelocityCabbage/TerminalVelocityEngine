package client;

import com.github.simplenet.Client;
import com.github.simplenet.packet.Packet;
import engine.debug.Log;
import org.fusesource.jansi.AnsiConsole;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameClient {

	String id;

	public GameClient(String id) {
		this.id = id;
	}

	public void start() {
		//Enable Console colors
		AnsiConsole.systemInstall();

		// Initialize a new client.
		Client client = new Client();

		// This callback is invoked when this client connects to a server.
		client.onConnect(() -> {
			var scanner = new Scanner(System.in);

			// If messages arrive from other clients, print them to the console.
			client.readStringAlways(System.out::println);

			System.out.print("Enter your nickname: ");
			Packet.builder().putByte(1).putString(scanner.nextLine()).queueAndFlush(client);

			// Infinite loop to accept user-input for the chat server.
			while (true) {
				// Read the client's message from the console.
				var message = scanner.nextLine();

				// If this client types "/leave", close their connection to the server.
				if ("/leave".equals(message)) {
					client.close();
					break;
				}

				// Otherwise, send a packet to the server containing the client's message.
				Packet.builder().putByte(2).putString(message).queueAndFlush(client);
			}
		});

		// Attempt to connect to a server AFTER registering listeners.
		client.connect("localhost", 49056);
	}
}
