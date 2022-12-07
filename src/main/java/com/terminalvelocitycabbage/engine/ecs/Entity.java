package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.pooling.Poolable;

import java.util.HashMap;
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

    public Entity() {
        components = new HashMap<>();
        reset();
    }

    /**
     * Adds a component to this entity
     * @param component The component to be added to this entity
     */
    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    /**
     * @param componentClass The class of the component you want to retrieve from this entity
     * @param <T> A class that implements {@link Component}
     * @return The component requested or null
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> T getComponent(Class<T> componentClass) {
        if (!containsComponent(componentClass)) return null;
        return (T) components.get(componentClass);
    }

    /**
     * Tests whether a component exists on this entity
     *
     * @param componentClass The class of the component you want to know if this entity possesses
     * @param <T> A class that implements {@link Component}
     * @return A boolean representing whether this entity contains the specified component
     */
    public <T extends Component> boolean containsComponent(Class<T> componentClass) {
        return components.containsKey(componentClass);
    }

    /**
     * Removes a component from this entity
     *
     * @param componentClass The class of the component you wish to remove
     * @param <T> Any class that implements {@link Component}
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        components.remove(componentClass);
    }

    /**
     * Removes all components from this entity
     */
    public void removeAllComponents() {
        components.clear();
    }

    /**
     * @return the unique identifier for this entity
     */
    public UUID getID() {
        return uniqueID;
    }

    /**
     * resets this entity to it's empty usable state. This usually occurs when this entity is added back to the
     * entity pool for later use. It assigns this entity a new uuid and clears all components
     */
    @Override
    public void reset() {
        uniqueID = UUID.randomUUID();
        removeAllComponents();
    }
}
