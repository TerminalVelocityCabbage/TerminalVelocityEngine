package com.terminalvelocitycabbage.engine.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.client.networking.PingClient;
import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import com.terminalvelocitycabbage.engine.debug.Logger;
import com.terminalvelocitycabbage.engine.events.EventDispatcher;
import com.terminalvelocitycabbage.engine.events.client.ClientConnectionEvent;
import com.terminalvelocitycabbage.engine.events.client.ClientStartEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class ClientBase extends EventDispatcher {

	String id;
	Client client;
	boolean shouldDisconnect;
	public static ClientBase instance;
	private Logger logger;
	private static Renderer renderer;

	public ClientBase(Logger logger, Renderer renderer) {
		this.logger = logger;
		ClientBase.renderer = renderer;
	}

	public void init() {
		preInit();

		//Start up the network listeners
		client = new Client();
		dispatchEvent(new ClientStartEvent(ClientStartEvent.INIT, client));
		postInit();
	}

	private void preInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.PRE_INIT, client));
	}

	private void postInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.POST_INIT, client));
	}

	public void start() {
		//client.onConnect(() -> dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.CONNECT, client)));
		//client.preDisconnect(() -> dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_DISCONNECT, client)));
		//client.postDisconnect(() -> dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_DISCONNECT, client)));
		dispatchEvent(new ClientStartEvent(ClientStartEvent.START, client));
	}

	public void connect(String address, int port) {
		//client.connect(address, port);
		//shouldDisconnect = false;
	}

	public void disconnect() {
		//shouldDisconnect = true;
		//client.close();
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

	public Logger getLogger() {
		return logger;
	}

	public static Renderer getRenderer() {
		return renderer;
	}
}
