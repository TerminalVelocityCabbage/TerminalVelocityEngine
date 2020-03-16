package engine.server;

import com.github.simplenet.Server;
import com.github.simplenet.packet.Packet;
import engine.debug.Log;
import engine.entity.Player;
import engine.events.EventDispatcher;
import engine.events.server.*;
import org.fusesource.jansi.AnsiConsole;

public abstract class ServerBase extends EventDispatcher {

	String id;
	Server server;
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
		server = new Server();
		dispatchEvent(new ServerStartEvent(ServerStartEvent.INIT, server));
		postInit();
	}

	//Basically set the server files up before starting any connections
	public void preInit() {
		dispatchEvent(new ServerStartEvent(ServerStartEvent.PRE_INIT, server));
	}

	public void postInit() {
		dispatchEvent(new ServerStartEvent(ServerStartEvent.POST_INIT, server));
	}

	//Binds the server and begins allowing connections
	public void start() {

		//dispatch events
		server.onConnect(client -> {
			dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.CONNECT, server, client));
		});

		//Register server listeners
		server.onConnect(client -> {
			client.preDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.PRE_DISCONNECT, server, client));
			});

			//When a client disconnects remove them from the map
			client.postDisconnect(() -> {
				super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.POST_DISCONNECT, server, client));
			});

			//Read bytes as they come in one at a time
			client.readByteAlways(opcode -> {
				switch (opcode) {
					case PacketTypes.COMMAND:
						client.readString(arguments -> {
							super.dispatchEvent(new ServerCommandReceivedEvent(ServerCommandReceivedEvent.RECEIVED, client, arguments));
						});
						break;
					case PacketTypes.CLIENT_VALIDATION:
						client.readString(username -> {
							super.dispatchEvent(new ServerClientPacketReceivedEvent(ServerClientPacketReceivedEvent.RECEIVED, client, username));
						});
						break;
					case PacketTypes.CHAT:
						client.readString(message -> {
							super.dispatchEvent(new ServerChatEvent(ServerChatEvent.RECEIVED, client, message));
						});
						break;
				}
			});
		});

		dispatchEvent(new ServerStartEvent(ServerStartEvent.START, server));
	}

	public void bind(String address, int port) {
		dispatchEvent(new ServerBindEvent(ServerBindEvent.PRE, server));
		server.bind(address, port);
		dispatchEvent(new ServerBindEvent(ServerBindEvent.POST, server));
		while (!shouldClose) {
			continue;
		}
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
