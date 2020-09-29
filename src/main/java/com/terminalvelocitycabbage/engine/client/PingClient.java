package com.terminalvelocitycabbage.engine.client;

import com.github.simplenet.Client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class PingClient {

	private final String address;
	private final int port;

	public PingClient(String address, int port) {
		this.address = address;
		this.port = port;
	}

	public PingResult ping() throws InterruptedException {
		var client = new Client();
		var latch = new CountDownLatch(1);
		var result = new AtomicBoolean(false);
		long pre = System.currentTimeMillis();

		client.onConnect(() -> {
			client.close();
			result.set(true);
			latch.countDown();
		});
		client.connect(this.address, this.port, 30, TimeUnit.SECONDS, latch::countDown);

		latch.await();
		return new PingResult(result.get(), System.currentTimeMillis() - pre);
	}
}
