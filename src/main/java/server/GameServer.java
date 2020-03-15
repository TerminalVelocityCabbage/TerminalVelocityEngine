package server;

import com.github.simplenet.Client;
import com.github.simplenet.packet.Packet;
import engine.command.Command;
import engine.command.CommandParser;
import engine.command.CommandResult;
import engine.command.CommandStorage;
import engine.debug.Log;
import engine.entity.Player;
import engine.server.ServerBase;

import java.util.concurrent.ConcurrentHashMap;

public class GameServer extends ServerBase {

	private CommandStorage commandStorage;

	public GameServer(String id) {
		super(id);
	}

	@Override
	public void init() {
		super.init();

		//Create a map of the clients and their usernames
		var clients = new ConcurrentHashMap<Client, Player>();

		getServer().onConnect(client -> {

			Log.info("Client attempting to establish connection.");

			//When a client disconnects remove them from the map
			client.postDisconnect(() -> {
				Log.info(clients.get(client).getUsername() + " left.");
				clients.remove(client);
			});

			//Initialize Command Storage
			this.commandStorage = new CommandStorage();

			//Create a command
			getCommandStorage().addCommand(
					Command.builder()
							.alias("hello", "hi")
							.addSubCommand(
									Command.builder()
											.alias("all")
											.executes((player, arguments) -> {
												getServer().queueAndFlushToAllExcept(Packet.builder().putString(player.getUsername() + ": said hi."), client);
												return CommandResult.success();
											})
											.build()
							)
							.executes((player, arguments) -> {
								Log.info(player.getUsername() + " said hello.");
								return CommandResult.success();
							})
							.build()
			);

			//Read bytes as they come in one at a time
			client.readByteAlways(opcode -> {
				switch (opcode) {
					case 0: //Command
						client.readString(arguments -> {
							CommandResult result = CommandParser.parse(arguments, clients.get(client));
							if (!result.wasSuccessful()) {
								Packet.builder().putString("Error: " + result.getFeedback()).queueAndFlush(client);
							}
						});
						break;
					case 1: //Change nickname
						client.readString(username -> {
							clients.put(client, new Player(username));
							Log.info("Client packet received: " + username + " successfully joined.");
						});
						break;
					case 2: //Send message to connected clients
						client.readString(message -> {
							message = clients.get(client).getUsername() + ": " + message;
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

	public CommandStorage getCommandStorage() {
		return commandStorage;
	}
}
