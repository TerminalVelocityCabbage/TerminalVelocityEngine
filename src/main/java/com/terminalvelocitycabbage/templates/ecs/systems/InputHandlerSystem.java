package com.terminalvelocitycabbage.templates.ecs.systems;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.System;
import com.terminalvelocitycabbage.templates.ecs.components.InputListenerComponent;

import java.util.List;

public class InputHandlerSystem extends System {

    @Override
    public List<Entity> getEntities() {
        return getManager().getMatchingEntities(ComponentFilter.builder().oneOf(InputListenerComponent.class).build());
    }

    @Override
    public void update(float deltaTime) {
        for (Entity entity : getEntities()) {
            entity.getComponent(InputListenerComponent.class).setCurrentInputFrame(ClientBase.getWindow().getInputListener().getCurrentInputFrame());
        }
    }
}
