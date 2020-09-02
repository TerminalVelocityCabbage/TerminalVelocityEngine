package engine.entity;

import java.util.UUID;

public class Player {

	//TODO this is temporary this needs to be abstracted probably
	private UUID uuid;
	private String username;

	public Player(String username) {
		this.username = username;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getUsername() {
		return username;
	}
}
