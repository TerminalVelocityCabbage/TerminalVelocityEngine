package com.terminalvelocitycabbage.engine.client;

import com.github.simplenet.Client;
import com.terminalvelocitycabbage.engine.events.EventDispatcher;
import com.terminalvelocitycabbage.engine.events.client.ClientConnectionEvent;
import com.terminalvelocitycabbage.engine.events.client.ClientStartEvent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class ClientBase extends EventDispatcher {

	String id;
	Client client;
	boolean shouldDisconnect;

	public ClientBase() {}

	public void init() {
		preInit();

		//Start up the network listeners
		client = new Client();
		dispatchEvent(new ClientStartEvent(ClientStartEvent.INIT.getIdentifier(), client));
		postInit();
	}

	private void preInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.PRE_INIT.getIdentifier(), client));
	}

	private void postInit() {
		dispatchEvent(new ClientStartEvent(ClientStartEvent.POST_INIT.getIdentifier(), client));
	}

	public void start() {

		client.onConnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.CONNECT.getIdentifier(), client));
		});

		client.preDisconnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_DISCONNECT.getIdentifier(), client));
		});

		client.postDisconnect(() -> {
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_DISCONNECT.getIdentifier(), client));
		});

		dispatchEvent(new ClientStartEvent(ClientStartEvent.START.getIdentifier(), client));
	}

	public void connect(String address, int port) {
		client.connect(address, port);
		shouldDisconnect = false;
	}

	public void disconnect() {
		shouldDisconnect = true;
		client.close();
	}

	public boolean disconnected() {
		return shouldDisconnect;
	}

	public void reconnect(String address, int port, int delay, int tries) throws InterruptedException {

		dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.PRE_RECONNECT.getIdentifier(), client));

		var latch = new CountDownLatch(tries);
		var pingClient = new PingClient(address, port);
		boolean shouldConnect = false;

		while (tries > 0){
			latch.await(delay, TimeUnit.SECONDS);
			if (pingClient.ping().getResult()) {
				shouldConnect = true;
				break;
			}
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.RECONNECT_TRY_FAIL.getIdentifier(), client));
			latch.countDown();
			tries--;
		}

		if (shouldConnect) {
			disconnect();
			init();
			start();
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.POST_RECONNECT.getIdentifier(), client));
		} else {
			disconnect();
			dispatchEvent(new ClientConnectionEvent(ClientConnectionEvent.RECONNECT_FAIL.getIdentifier(), client));
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getID() {
		return this.id;
	}
}
