package com.terminalvelocitycabbage.engine.server;

import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.events.EventDispatcher;
import com.terminalvelocitycabbage.engine.events.server.*;
import com.terminalvelocitycabbage.engine.scheduler.Scheduler;
import com.terminalvelocitycabbage.engine.server.packet.PacketTypes;
import com.terminalvelocitycabbage.engine.utils.TickManager;
import com.terminalvelocitycabbage.templates.events.server.*;

public abstract class ServerBase extends EventDispatcher {

	Server server;
	//TODO should close should instead be a packet to call server.close() but I need to do it safely.
	private boolean shouldClose;
	String address;
	int port;
	private static Scheduler scheduler;
	private TickManager tickManager;
	long lastTime;

	public ServerBase(int ticksPerSecond) {
		shouldClose = false;
		scheduler = new Scheduler();
		tickManager = new TickManager(ticksPerSecond);
	}

	public void init() {
		preInit();
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

		dispatchEvent(new ServerBindEvent(ServerBindEvent.PRE, getServer()));
		getServer().bind(getAddress(), getPort());
		dispatchEvent(new ServerBindEvent(ServerBindEvent.POST, getServer()));

		dispatchEvent(new ServerStartEvent(ServerStartEvent.START, getServer()));

		//Run the Server
		while (!shouldClose) {
			tickManager.apply(System.currentTimeMillis() - lastTime);
			while (tickManager.hasTick()) {
				scheduler.tick();
			}
			lastTime = System.currentTimeMillis();
		}
	}

	public Server getServer() {
		return this.server;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setShouldClose(boolean shouldClose) {
		this.shouldClose = shouldClose;
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}
}
