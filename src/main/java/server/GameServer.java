package server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;
import engine.command.Command;
import engine.command.CommandParser;
import engine.command.CommandResult;
import engine.command.CommandStorage;
import engine.debug.Log;
import engine.entity.Player;
import engine.events.HandleEvent;
import engine.events.server.ServerClientConnectionEvent;
import engine.server.ServerBase;

import java.util.concurrent.ConcurrentHashMap;

public class GameServer extends ServerBase {

	private CommandStorage commandStorage;
	private ConcurrentHashMap<Client, Player> clients;

	public GameServer(String id) {
		super(id);
	}

	@Override
	public void init() {
		super.init();
		super.addEventHandler(this);
		//Create a map of the clients and their usernames
		this.clients = new ConcurrentHashMap<>();
		this.start();
	}

	@Override
	public void start() {
		super.start();
	}

	public CommandStorage getCommandStorage() {
		return commandStorage;
	}

	@HandleEvent(ServerClientConnectionEvent.CONNECT)
	public void onClientConnect(ServerClientConnectionEvent event) {
		Log.info("Client attempting to establish connection.");

		Client client = event.getClient();
		Server server = event.getServer();

		client.preDisconnect(() -> {
			super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.PRE_DISCONNECT, server, client));
		});

		//When a client disconnects remove them from the map
		client.postDisconnect(() -> {
			super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.POST_DISCONNECT, server, client));
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
						} else {
							if (result.getFeedback().length() > 0) {
								Packet.builder().putString(result.getFeedback()).queueAndFlush(client);
							}
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
	}

	@HandleEvent(ServerClientConnectionEvent.POST_DISCONNECT)
	public void postDisconnect(ServerClientConnectionEvent event) {
		//TODO check to see if the client ever achieved player status
		Log.info(clients.get(event.getClient()).getUsername() + " left.");
		clients.remove(event.getClient());
	}
}
