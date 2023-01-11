package com.terminalvelocitycabbage.engine.client.renderer.scenes;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.window.Window;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.templates.ecs.components.ModelComponent;

public abstract class Scene {

	public Scene() { }

	public abstract void init(Window window);

	public abstract void tick(float deltaTime);

	public abstract void destroy();

	public Manager getManager() {
		return ClientBase.getInstance().getManager();
	}

	/**
	 * gets all entities in this manager with the model component and binds the models so that the renderer has access.
	 */
	protected void bindEntitiesWithModels() {
		getManager().getMatchingEntities(ComponentFilter.builder().oneOf(ModelComponent.class).build())
				.forEach(entity -> entity.getComponent(ModelComponent.class).getModel().bind());
	}

	/**
	 * gets all entities in this manager with a model component and destroys the model for cleanup.
	 */
	protected void destroyModelsOfAllEntities() {
		getManager().getMatchingEntities(ComponentFilter.builder().oneOf(ModelComponent.class).build())
				.forEach(entity -> entity.getComponent(ModelComponent.class).getModel().destroy());
	}
}
