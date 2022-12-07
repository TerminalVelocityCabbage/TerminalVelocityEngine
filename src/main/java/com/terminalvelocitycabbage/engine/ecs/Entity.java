package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.pooling.Poolable;

import java.util.Map;
import java.util.UUID;

/**
 * An entity is meant to be a container for components, provided is a Collection for your components
 * the entity also carries a unique identifier in case you need to retrieve a specific entity at any point in time.
 */
public class Entity implements Poolable {

    //The unique identifier of this entity
    private UUID uniqueID;
    //The container of components that defined this entity
    private Map<Class<?>, Component> components;

    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        return (T) components.get(componentClass);
    }

    public <T extends Component> boolean containsComponent(Class<T> componentClass) {
        return components.containsKey(componentClass);
    }

    public <T extends Component> void remove(Class<T> componentClass) {
        components.remove(componentClass);
    }

    public void removeAll() {
        components.clear();
    }

    public UUID getID() {
        return uniqueID;
    }

    @Override
    public void reset() {
        uniqueID = UUID.randomUUID();
        removeAll();
    }
}
