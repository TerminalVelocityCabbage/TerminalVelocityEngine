package com.terminalvelocitycabbage.templates.networking;

public class PingResult {

	boolean result;
	long time;

	public PingResult(boolean result, long time) {
		this.result = result;
		this.time = time;
	}

	public boolean getResult() {
		return result;
	}

	public long getTime() {
		return time;
	}
}
