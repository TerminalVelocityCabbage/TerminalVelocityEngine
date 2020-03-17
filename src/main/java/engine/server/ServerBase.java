package engine.server;

import com.github.simplenet.Server;
import engine.configs.Config;
import engine.events.EventDispatcher;
import engine.events.server.*;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.IOException;

public abstract class ServerBase extends EventDispatcher {

	String id;
	Server server;
	//TODO should close should instead be a packet to call server.close() but I need to do it safely.
	private boolean shouldClose;

	public ServerBase(String id) {
		this.id = id;
		shouldClose = false;
	}

	public void init() {
		preInit();
		//Enable Console colors
		AnsiConsole.systemInstall();

		//Start up the network listeners
		this.server = new Server();
		dispatchEvent(new ServerStartEvent(ServerStartEvent.INIT, getServer()));
		postInit();
	}

	//Basically set the server files up before starting any connections
	public void preInit() {
		dispatchEvent(new ServerStartEvent(ServerStartEvent.PRE_INIT, getServer()));
	}

	public void postInit() {
		dispatchEvent(new ServerStartEvent(ServerStartEvent.POST_INIT, getServer()));
	}

	//Binds the server and begins allowing connections
	public void start() {

		//dispatch events
		//Register server listeners
		getServer().onConnect(client -> {

			dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.CONNECT, getServer(), client));

			client.preDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.PRE_DISCONNECT, getServer(), client));
			});

			client.postDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.POST_DISCONNECT, getServer(), client));
			});

			//Read packets and dispatch events based on opcode
			client.readByteAlways(opcode -> {
				switch (opcode) {
					case PacketTypes.CLIENT_VALIDATION:
						client.readString(username -> {
							super.dispatchEvent(new ServerClientPacketReceivedEvent(ServerClientPacketReceivedEvent.RECEIVED, client, username));
						});
						break;
					case PacketTypes.CHAT:
						client.readString(message -> {
							if (message.startsWith("/")) {
								super.dispatchEvent(new ServerCommandReceivedEvent(ServerCommandReceivedEvent.RECEIVED, client, message));
							} else {
								super.dispatchEvent(new ServerChatEvent(ServerChatEvent.RECEIVED, client, message));
							}
						});
						break;
				}
			});
		});

		//Load the server's config file into a usable object
		try {
			Config config = Config.load("saves" + File.separator + getId() , "server");
			//Bind the server to the configured port and IP
			//TODO binding needs it's own system so that an event can pass information back to the ServerBase before post bind if it needs information pre bind
			dispatchEvent(new ServerBindEvent(ServerBindEvent.PRE, getServer()));
			getServer().bind(config.getOptions().get("ip"), Integer.parseInt(config.getOptions().get("port")));
			dispatchEvent(new ServerBindEvent(ServerBindEvent.POST, getServer()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		dispatchEvent(new ServerStartEvent(ServerStartEvent.START, getServer()));
	}

	public Server getServer() {
		return this.server;
	}

	public String getId() {
		return this.id;
	}

	public void setShouldClose(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}
}
