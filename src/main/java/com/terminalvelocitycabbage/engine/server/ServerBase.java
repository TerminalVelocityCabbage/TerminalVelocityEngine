package com.terminalvelocitycabbage.engine.server;

import com.github.simplenet.Client;
import com.github.simplenet.Server;
import com.terminalvelocitycabbage.engine.debug.Logger;
import com.terminalvelocitycabbage.engine.debug.LoggerSource;
import com.terminalvelocitycabbage.engine.events.EventDispatcher;
import com.terminalvelocitycabbage.engine.networking.SidedEntrypoint;
import com.terminalvelocitycabbage.engine.networking.packet.SerializablePacket;
import com.terminalvelocitycabbage.engine.scheduler.Scheduler;
import com.terminalvelocitycabbage.engine.utils.TickManager;
import com.terminalvelocitycabbage.templates.events.server.ServerClientConnectionEvent;
import com.terminalvelocitycabbage.templates.events.server.ServerLifecycleEvent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public abstract class ServerBase extends EventDispatcher implements SidedEntrypoint, LoggerSource {

	protected static ServerBase instance;
	Server server;
	private boolean shouldClose;
	String address;
	int port;
	final private static Scheduler scheduler = new Scheduler();
	final private TickManager tickManager;
	long lastTime;
	private Logger logger;

	public ServerBase(Logger logger, int ticksPerSecond) {
		shouldClose = false;
		tickManager = new TickManager(ticksPerSecond);
		this.logger = logger;
	}

	public static ServerBase getInstance() {
		return instance;
	}

	public void run() {

		//Initialize server and broadcast lifecycle events for initialization
		//The stage just before the server instance is created
		preInit();
		//The stage just after the server instance is created
		init();

		//Register server listeners that listen for packets and client connections
		registerListeners();

		//Bind this server to its address and port and dispatch events related to this lifecycle stage
		//The stage just before the server is bound to its address and port
		preBind();
		//The stage just after the server is bound to its address and port
		bind();

		//At this point most server related things can take place
		//The stage just before the game loop begins
		start();

		//Run the Server
		//The stage that runs while this server is not marked for closure, schedulers and ecs managers will be ticking
		loop();

		//Dispatch server stopping events both before and after we release the server info.
		//The phase just before the server stops
		stopping();
		//The phase just after the server stops
		stop();
	}

	protected void init() {
		this.server = new Server();
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.INIT, getServer()));
	}

	//Basically set the server files up before starting any connections
	protected void preInit() {
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.PRE_INIT, getServer()));
	}

	protected void registerListeners() {
		getServer().onConnect(client -> {

			onPlayerJoined(client);

			client.preDisconnect(() -> {
				onPlayerPreDisconnect(client);
			});

			client.postDisconnect(() -> {
				onPlayerDisconnected(client);
			});

			//Read packets and dispatch events based on opcode
			client.readIntAlways(opcode -> {
				client.readInt(bytesSize -> {
					client.readBytes(bytesSize, bytes -> {
						try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
							SerializablePacket received = (SerializablePacket) ois.readObject();
							received.interpretReceivedByServer(server, client);
						} catch (IOException | ClassNotFoundException e) {
							throw new RuntimeException(e);
						}
					});
				});
			});
		});
	}

	protected void onPlayerJoined(Client client) {
		dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.CONNECT, getServer(), client));
	}

	protected void onPlayerPreDisconnect(Client client) {
		super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.PRE_DISCONNECT, getServer(), client));
	}

	protected void onPlayerDisconnected(Client client) {
		super.dispatchEvent(new ServerClientConnectionEvent(ServerClientConnectionEvent.POST_DISCONNECT, getServer(), client));
	}

	protected void preBind() {
		//Allows the program creator to add tasks via override before plugin authors who can only listen to events
	}

	protected void bind() {
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.PRE_BIND, getServer()));
		getServer().bind(getAddress(), getPort());
	}

	protected void start() {
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.STARTED, getServer()));
	}

	protected void loop() {
		while (!shouldClose) {
			tickManager.apply(System.currentTimeMillis() - lastTime);
			while (tickManager.hasTick()) {
				scheduler.tick();
			}
			lastTime = System.currentTimeMillis();
		}
	}

	protected void stopping() {
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.STOPPING, getServer()));
	}

	protected void stop() {
		server.close();
		dispatchEvent(new ServerLifecycleEvent(ServerLifecycleEvent.STOPPED, getServer()));
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

	@Override
	public Logger getLogger() {
		return logger;
	}
}
