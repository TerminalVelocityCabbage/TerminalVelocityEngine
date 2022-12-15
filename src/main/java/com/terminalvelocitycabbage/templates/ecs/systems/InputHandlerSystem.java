package com.terminalvelocitycabbage.templates.ecs.systems;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.System;
import com.terminalvelocitycabbage.templates.ecs.components.ControllableComponent;

import java.util.List;

public class InputHandlerSystem extends System {

    @Override
    public List<Entity> getEntities() {
        return getManager().getMatchingEntities(ComponentFilter.builder().oneOf(ControllableComponent.class).build());
    }

    @Override
    public void update(float deltaTime) {
        getEntities().forEach(entity -> entity.getComponent(ControllableComponent.class).getInputHandler().tick());
    }
}
