package com.terminalvelocitycabbage.templates.ecs.systems.types;

import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.System;

public abstract class IteratingSystem extends System {

    @Override
    public void update(float deltaTime) {
        getEntities().forEach(entity -> updateItem(deltaTime, entity));
    }

    protected abstract void updateItem(float deltaTime, Entity entity);
}
