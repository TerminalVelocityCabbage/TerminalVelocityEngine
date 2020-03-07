import client.GameClient;
import engine.debug.Log;
import org.fusesource.jansi.AnsiConsole;
import server.GameServer;

import java.util.Scanner;

public class Launcher {

	public static void main(String[] args) {

		AnsiConsole.systemInstall();

		boolean isInvalid = true;

		Scanner input = new Scanner(System.in);

		do {
			System.out.println("Would you like to start a (s)erver or (c)lient?");
			String selection = input.nextLine();
			if (selection.equals("s")) {

				//Mark the argument type as valid
				isInvalid = false;

				//Create a server object with a given name
				System.out.println("What would you like to name this server?");
				GameServer gameServer = new GameServer(input.nextLine().replace(" ", "-"));

				//Let the server class handle itself now.
				Log.info("Launching Server.");
				gameServer.init();

			} else if (selection.equals("c")) {

				//Mark the argument type as valid
				isInvalid = false;

				//Create a client object with the given username
				//System.out.println("Enter a username:");
				//GameClient gameClient = new GameClient(input.nextLine().replace(" ", "-"));

				//Pass on further operation to the Client
				Log.info("Launching Client.");
				GameClient client = new GameClient("id");
				client.start();

			} else {
				Log.error("Invalid input", "pick either c for client or s for server.");
			}
		} while (isInvalid);

		AnsiConsole.systemUninstall();
	}

}
