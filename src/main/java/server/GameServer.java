package server;

import com.github.simplenet.Client;
import com.github.simplenet.packet.Packet;
import engine.command.Command;
import engine.command.CommandParser;
import engine.command.CommandResult;
import engine.command.CommandStorage;
import engine.configs.Config;
import engine.debug.Log;
import engine.entity.Player;
import engine.events.HandleEvent;
import engine.events.server.*;
import engine.saves.SaveManager;
import engine.server.PacketTypes;
import engine.server.ServerBase;

import java.io.File;
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
											getServer().queueAndFlushToAllExcept(Packet.builder().putByte(PacketTypes.CHAT).putString(player.getUsername() + ": said hi."));
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
		Log.info("Server Started.");
	}

	@HandleEvent(ServerCommandReceivedEvent.RECEIVED)
	public void onCommand(ServerCommandReceivedEvent event) {
		CommandResult result = CommandParser.parse(event.getCommand(), clients.get(event.getClient()));
		if (!result.wasSuccessful()) {
			Packet.builder().putByte(PacketTypes.CHAT).putString("Error: " + result.getFeedback()).queueAndFlush(event.getClient());
		} else {
			if (result.getFeedback().length() > 0) {
				Packet.builder().putByte(PacketTypes.CHAT).putString(result.getFeedback()).queueAndFlush(event.getClient());
			}
		}
	}

	@HandleEvent(ServerClientPacketReceivedEvent.RECEIVED)
	public void onClientValidation(ServerClientPacketReceivedEvent event) {
		clients.put(event.getClient(), new Player(event.getUsername()));
		Log.info("Client packet received: " + event.getUsername() + " successfully joined.");
	}

	//TODO make this use a chat opcode
	@HandleEvent(ServerChatEvent.RECEIVED)
	public void onChat(ServerChatEvent event) {
		getServer().queueAndFlushToAllExcept(Packet.builder().putByte(PacketTypes.CHAT).putString(clients.get(event.getClient()).getUsername() +
				": " + event.getMessage()), event.getClient());
	}

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
