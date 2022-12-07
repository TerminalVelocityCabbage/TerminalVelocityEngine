package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The Manager is what any implementation should interact with to "manage" their entities components and systems.
 * This is where you register your systems, add your entities, and store your components.
 */
public class Manager {

    //The list of created components that can be added to any entity
    Set<Component> componentTypeSet; //TODO make this un-editable once the manager is done initializing as to
    //The list of entity types that can be "spawned" or added to the activeEntities List
    Set<Entity> entityTypeSet;

    //The list of active entities in this manager
    List<Entity> activeEntities;

    //The list of systems that runs on this manager
    List<System> systems;

    //TODO entity pool
    //TODO component pool

    /**
     * Adds a component to the componentTypeSet
     * @param componentType the class of the component you wish to add to the pool
     * @param <T> The type of the component, must extend {@link Component}
     */
    public <T extends Component> void createComponent(Class<T> componentType) {
        try {
            componentTypeSet.add(componentType.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.crash("Could not Create Component", new RuntimeException(e));
        }
    }

    /**
     * Gets a component of the type requested from the componentTypeSet
     * @param type the class of the component you wish to retrieve
     * @param <T> any component which extends {@link Component}
     * @return a component object from the componentTypeSet of the type requested.
     */
    public <T extends Component> T getComponent(Class<T> type) {
        List<T> list = componentTypeSet.stream().filter(type::isInstance).map(component -> (T) component).toList();
        if (list.size() > 1) Log.warn("Multiple components of same type exist in component collection");
        if (list.size() == 1) return list.get(0); //TODO init the component with default values
        return null;
    }

    /**
     * updates all {@link System}s in this manager
     * @param deltaTime the amount of time in milliseconds that has passed since the last update
     */
    public void update(float deltaTime) {
        systems.forEach(system -> {
            system.update(deltaTime);
        });
    }

    /**
     * Gets all entities that match the provided filter
     * @param filter the filter for which you want to get matching entities
     * @return a List of entities that match the filter provided
     */
    public List<Entity> getMatchingEntities(ComponentFilter filter) {
        return filter.filter(activeEntities);
    }

    /**
     * Gets the entity in this Manager with the specified ID if it exists
     *
     * @param id the UUID of this entity (you can use UUID.fromString() to get this if you only have a string)
     * @return the entity requested or null
     */
    public Entity getEntityWithID(UUID id) {
        for (Entity entity : activeEntities) {
            if (entity.getID().equals(id)) return entity;
        }
        return null;
    }
}
