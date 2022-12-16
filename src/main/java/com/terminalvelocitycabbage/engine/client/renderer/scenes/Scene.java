package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.Window;
import com.terminalvelocitycabbage.engine.ecs.Manager;

public abstract class Scene {

	public Scene() { }

	public abstract void init(Window window);

	public abstract void tick(float deltaTime);

	public abstract void destroy();

	public Manager getManager() {
		return ClientBase.instance.getManager();
	}
}
