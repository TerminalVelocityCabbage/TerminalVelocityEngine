package com.terminalvelocitycabbage.engine.ecs;

import java.util.List;
import java.util.UUID;

/**
 * An entity is meant to be a container for components, provided is a Collection for your components
 * the entity also carries a unique identifier in case you need to retrieve a specific entity at any point in time.
 */
public abstract class Entity {

    //The unique identifier of this entity
    UUID uniqueID;
    //The container of components that defined this entity
    private List<Component> components;
}
