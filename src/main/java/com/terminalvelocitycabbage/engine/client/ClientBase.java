package com.terminalvelocitycabbage.engine.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.client.renderer.Window;
import com.terminalvelocitycabbage.engine.client.renderer.scenes.SceneHandler;
import com.terminalvelocitycabbage.engine.client.sound.SoundDeviceManager;
import com.terminalvelocitycabbage.engine.client.state.StateHandler;
import com.terminalvelocitycabbage.engine.client.ui.ScreenHandler;
import com.terminalvelocitycabbage.engine.debug.Logger;
import com.terminalvelocitycabbage.engine.debug.LoggerSource;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.events.EventDispatcher;
import com.terminalvelocitycabbage.engine.networking.SidedEntrypoint;
import com.terminalvelocitycabbage.engine.networking.packet.SerializablePacket;
import com.terminalvelocitycabbage.engine.networking.packet.SyncPacketRegistryPacket;
import com.terminalvelocitycabbage.engine.scheduler.Scheduler;
import com.terminalvelocitycabbage.templates.events.client.ClientConnectionEvent;
import com.terminalvelocitycabbage.templates.events.client.ClientStartEvent;
import com.terminalvelocitycabbage.templates.networking.PingClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class ClientBase extends EventDispatcher implements SidedEntrypoint, LoggerSource {

	String id;
	Client client;
	private static Window window;
	boolean shouldDisconnect;
	protected static ClientBase instance;
	private Logger logger;
	private static Renderer renderer;
	private final SceneHandler sceneHandler = new SceneHandler();
	private static SoundDeviceManager soundDeviceManager;
	private static Scheduler scheduler;
	Manager manager;
	boolean debugMode;
	StateHandler stateHandler;
	ScreenHandler screenHandler;

	public ClientBase(Logger logger, Renderer renderer, boolean debugMode) {
		this.debugMode = debugMode;
		this.logger = logger;
		ClientBase.soundDeviceManager = new SoundDeviceManager();
		ClientBase.renderer = renderer;
		ClientBase.renderer.setDebugMode(debugMode);
		scheduler = new Scheduler();
		manager = new Manager();
		stateHandler = new StateHandler();
		screenHandler = new ScreenHandler();
	}

	public void init() {
		preInit();

		setWindow(new Window(1900, 1000, "Outergrowth", false, true, true));
		getWindow().create(isDebugMode());
		getWindow().init();
		getWindow().show();

		//Start up the network listeners
		client = new Client();
		client.onConnect(this::onConnect);
		client.preDisconnect(this::onPreDisconnect);
		client.postDisconnect(this::onDisconnected);
		dispatchEvent(new ClientStartEvent(ClientStartEvent.INIT, client));
		getWindow().init();
		getRenderer().init();
	}

	public void preInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.PRE_INIT, client));
	}

	public void postInit() {
		getScreenHandler().init();
		dispatchEvent(new ClientStartEvent(ClientStartEvent.POST_INIT, client));
	}

	public void start() {
		postInit();
		dispatchEvent(new ClientStartEvent(ClientStartEvent.START, client));
		getRenderer().run();
	}

	public void onConnect() {
		client.readIntAlways(opcode -> {
			client.readInt(bytesSize -> {
				client.readBytes(bytesSize, bytes -> {
					try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); ObjectInputStream ois = new ObjectInputStream(bis)) {
						SerializablePacket received = (SerializablePacket) ois.readObject();
						received.interpretReceivedByClient(client);
					} catch (IOException | ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
				});
			});
		});
		requestOpcodesSync();
		dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.CONNECT, client));
	}

	public void requestOpcodesSync() {
		sendPacket(new SyncPacketRegistryPacket(), SyncPacketRegistryPacket.class);
	}

	public void onPreDisconnect() {
		dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_DISCONNECT, client));
	}

	public void onDisconnected() {
		dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_DISCONNECT, client));
	}

	public void cleanup() {
		getRenderer().destroy();
		getScreenHandler().cleanup();
		getSceneHandler().cleanup();
		getSoundDeviceManager().cleanup();
		getWindow().destroy();
	}

	public void tick(float deltaTime) {
		getScheduler().tick();
		getSceneHandler().getActiveScene().tick(deltaTime);
		getStateHandler().tick();
	}

	public void connect(String address, int port) {
		client.connect(address, port);
		shouldDisconnect = false;
	}

	public void disconnect() {
		shouldDisconnect = true;
		client.close();
	}

	public boolean shouldDisconnect() {
		return shouldDisconnect;
	}

	public void reconnect(String address, int port, int delay, int tries) throws InterruptedException {

		dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_RECONNECT, client));

		var latch = new CountDownLatch(tries);
		var pingClient = new PingClient(address, port);
		boolean shouldConnect = false;

		while (tries > 0){
			latch.await(delay, TimeUnit.SECONDS);
			if (pingClient.ping().getResult()) {
				shouldConnect = true;
				break;
			}
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.RECONNECT_TRY_FAIL, client));
			latch.countDown();
			tries--;
		}

		disconnect();
		if (shouldConnect) {
			init();
			start();
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_RECONNECT, client));
		} else {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.RECONNECT_FAIL, client));
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	public static SoundDeviceManager getSoundDeviceManager() {
		return soundDeviceManager;
	}

	public static Renderer getRenderer() {
		return renderer;
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}

	public Manager getManager() {
		return manager;
	}

	public SceneHandler getSceneHandler() {
		return sceneHandler;
	}

	public static ClientBase getInstance() {
		return instance;
	}

	public Client getClient() {
		return client;
	}

	public static Window getWindow() {
		return window;
	}

	public static void setWindow(Window window) {
		ClientBase.window = window;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public StateHandler getStateHandler() {
		return stateHandler;
	}

	public ScreenHandler getScreenHandler() {
		return screenHandler;
	}

	public void sendPacket(SerializablePacket packet, Class<? extends SerializablePacket> packetClass) {
		packet.pack(getInstance(), packetClass).queueAndFlush(client);
	}
}
