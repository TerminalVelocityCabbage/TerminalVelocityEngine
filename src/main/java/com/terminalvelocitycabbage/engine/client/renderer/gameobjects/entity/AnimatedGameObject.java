package com.terminalvelocitycabbage.engine.client.renderer.gameobjects.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AnimatedGameObject extends ModeledGameObject {

	List<UUID> animationUUIDs;

	public AnimatedGameObject() {
		this.animationUUIDs = new ArrayList<>();
	}

	public void update(float deltaTime) {
		//this.(AnimatedModel)model.handler
		super.update();
	}
}
