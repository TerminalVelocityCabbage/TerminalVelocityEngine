package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.pooling.Poolable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An entity is meant to be a container for components, provided is a Collection for your components
 * the entity also carries a unique identifier in case you need to retrieve a specific entity at any point in time.
 * Should be created with the {@link Manager#createEntity()} method to ensure that this entity is properly managed.
 */
public class Entity implements Poolable {

    //The manager that manages this entity
    private Manager manager;
    //The unique identifier of this entity
    private UUID uniqueID;
    //The container of components that defined this entity
    private Map<Class<?>, Component> components;

    /**
     * Creates a new entity object, this should not be instantiated on its own but should rather be created with
     * the {@link Manager#createEntity()} method so that the entity can interact with the manager's pools.
     */
    protected Entity() {
        components = new HashMap<>();
        uniqueID = UUID.randomUUID();
    }

    /**
     * @param manager the manager of this entity
     */
    protected void setManager(Manager manager) {
        this.manager = manager;
    }

    public <T extends Component> T addComponent(Class<T> componentClass) {
        if (containsComponent(componentClass)) {
            Log.warn("Tried to add component " + componentClass.getName() + " to entity with id " + getID() + " which already contains it");
        }
        components.put(componentClass, manager.obtainComponent(componentClass));
        return getComponent(componentClass);
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
        getComponent(componentClass).setDefaults();
        manager.componentPool.free(getComponent(componentClass));
        components.remove(componentClass);
    }

    /**
     * Removes all components from this entity
     */
    public void removeAllComponents() {
        //if (manager != null)
        manager.componentPool.free(components);
        components.clear();
    }

    /**
     * @return the unique identifier for this entity
     */
    public UUID getID() {
        return uniqueID;
    }

    /**
     * @return a boolean to represent whether this entity has any components
     */
    public boolean isEmpty() {
        return components.size() == 0;
    }

    /**
     * resets this entity to it's empty usable state. This usually occurs when this entity is added back to the
     * entity pool for later use. It assigns this entity a new uuid and clears all components
     */
    @Override
    public void setDefaults() {
        uniqueID = UUID.randomUUID();
    }
}
