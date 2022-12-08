package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.utils.ClassUtils;

import javax.management.ReflectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * The Manager is what any implementation should interact with to "manage" their entities components and systems.
 * This is where you register your systems, add your entities, and store your components.
 */
public class Manager {

    //The list of created components that can be added to any entity
    Set<Component> componentTypeSet; //TODO make this un-editable once the manager is done initializing as to

    //The list of active entities in this manager
    List<Entity> activeEntities;

    //The list of systems that runs on this manager
    Map<Class<? extends System>, System> systems;

    //TODO entity pool
    //TODO component pool

    public Manager() {
        componentTypeSet = new HashSet<>();
        activeEntities = new ArrayList<>();
        systems = new HashMap<>();
    }

    /**
     * Adds a component to the componentTypeSet
     * @param componentType the class of the component you wish to add to the pool
     * @param <T> The type of the component, must extend {@link Component}
     */
    public <T extends Component> void registerComponent(Class<T> componentType) {
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
     * creates a new entity and adds it to the active entities list for modification later
     *
     * @return the newly created entity
     */
    public Entity createEntity() {
        Entity entity = new Entity(); //TODO obtain from pool
        activeEntities.add(entity);
        return entity;
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

    /**
     * Creates and registers a system of class type specified
     *
     * @param systemClass The class for the type of system you wish to create
     * @param <T> The class for the type of system you want to create
     * @param priority The priority that this system takes (the order it executes in). Lower numbers execute first.
     * @return The system you just created
     */
    public <T extends System> T createSystem(Class<T> systemClass, int priority) {
        try {
            T system = ClassUtils.createInstance(systemClass);
            systems.put(systemClass, system);
            system.setManager(this);
            system.setPriority(priority);
            return system;
        } catch (ReflectionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates and registers a system of class type specified
     *
     * @param systemClass The class for the type of system you wish to create
     * @param <T> The class for the type of system you want to create
     * @return The system you just created
     */
    public <T extends System> T createSystem(Class<T> systemClass) {
        return createSystem(systemClass, 1);
    }

    /**
     * updates all {@link System}s in this manager in order of their priority
     * @param deltaTime the amount of time in milliseconds that has passed since the last update
     */
    public void update(float deltaTime) {
        systems.values().stream().sorted(System::compareTo).forEach(system -> system.update(deltaTime));
    }
}
