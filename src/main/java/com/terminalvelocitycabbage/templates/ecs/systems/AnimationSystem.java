package com.terminalvelocitycabbage.templates.ecs.systems;

import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.templates.ecs.components.AnimatedComponent;
import com.terminalvelocitycabbage.templates.ecs.systems.types.IteratingSystem;

import java.util.List;

public class AnimationSystem extends IteratingSystem {

    @Override
    public List<Entity> getEntities() {
        return getManager().getMatchingEntities(ComponentFilter.builder().oneOf(AnimatedComponent.class).build());
    }

    @Override
    protected void updateItem(float deltaTime, Entity entity) {
        entity.getComponent(AnimatedComponent.class).animate(deltaTime);
    }
}
