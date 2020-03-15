package server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;
import engine.command.Command;
import engine.command.CommandParser;
import engine.command.CommandResult;
import engine.command.CommandStorage;
import engine.configs.Config;
import engine.debug.Log;
import engine.entity.Player;
import engine.events.HandleEvent;
import engine.events.server.ServerClientConnectionEvent;
import engine.events.server.ServerStartEvent;
import engine.saves.SaveManager;
import engine.server.ServerBase;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GameServer extends ServerBase {

	private CommandStorage commandStorage;
	private ConcurrentHashMap<Client, Player> clients;

	public GameServer(String id) {
		super(id);
		addEventHandler(this);
	}

	public CommandStorage getCommandStorage() {
		return commandStorage;
	}

	@HandleEvent(ServerStartEvent.PRE_INIT)
	public void onPreInit(ServerStartEvent event) {
		//Check for and load or create new server configuration
		Log.info("Server Starting with ID: " + getId());
		if (!SaveManager.checkForSaveDirectory(getId())) {
			Log.info("No save Directory found for this save, assuming its a new save.");
			if (SaveManager.createSaveDirectory(getId())) {
				Config config = Config.builder()
						.setFileName("server")
						.setPath("saves" + File.separator + getId())
						.addKey("ip", "localhost")
						.addKey("port", "49056")
						.addKey("motd", "Message of the Day!")
						.build();
				if (config.save()) {
					return;
				}
			}
		} else {
			Log.info("Found existing save with name " + getId() + " loading that save.");
			return;
		}
		Log.error("Something went wrong during server initialization, the server will not start.");
	}

	@HandleEvent(ServerStartEvent.INIT)
	public void onInit(ServerStartEvent event) {
		this.clients = new ConcurrentHashMap<>();

		//Initialize Command Storage
		this.commandStorage = new CommandStorage();

		//Create commands
		getCommandStorage().addCommand(
				Command.builder()
						.alias("hello", "hi")
						.addSubCommand(
								Command.builder()
										.alias("all")
										.executes((player, arguments) -> {
											getServer().queueAndFlushToAllExcept(Packet.builder().putString(player.getUsername() + ": said hi."));
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

		getCommandStorage().addCommand(
				Command.builder()
						.alias("stop")
						.executes(((player, arguments) -> {
							//TODO this needs to use a permission system probably
							setShouldClose(true);
							return CommandResult.success();
						}))
						.build()
		);
	}

	@HandleEvent(ServerStartEvent.START)
	public void onStart(ServerStartEvent event) {

		//Register server listeners
		event.getServer().onConnect(client -> {
			client.preDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.PRE_DISCONNECT, event.getServer(), client));
			});

			//When a client disconnects remove them from the map
			client.postDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.POST_DISCONNECT, event.getServer(), client));
			});

			//Read bytes as they come in one at a time
			//TODO make these fire events based on their opcode
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
		});

		//Load the server's config file into a usable object
		try {
			Config config = Config.load("saves" + File.separator + getId() , "server");
			//Bind the server to the configured port and IP
			bind(config.getOptions().get("ip"), Integer.parseInt(config.getOptions().get("port")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//TODO these client methods need to be added to the server before it's bound

	@HandleEvent(ServerClientConnectionEvent.CONNECT)
	public void onClientConnect(ServerClientConnectionEvent event) {
		Log.info("Client attempting to establish connection.");
	}

	@HandleEvent(ServerClientConnectionEvent.POST_DISCONNECT)
	public void postDisconnect(ServerClientConnectionEvent event) {
		//TODO check to see if the client ever achieved player status
		Log.info(clients.get(event.getClient()).getUsername() + " left.");
		clients.remove(event.getClient());
	}
}
