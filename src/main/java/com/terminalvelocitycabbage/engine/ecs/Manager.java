package com.terminalvelocitycabbage.engine.ecs;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

/**
 * The Manager is what any implementation should interact with to "manage" their entities components and systems.
 * This is where you register your systems, add your entities, and store your components.
 */
public class Manager {

    //The list of created components that can be added to any entity
    Set<Component> componentSet;
    //The list of entity types that can be "spawned" or added to the activeEntities List
    Set<Entity> entitySet;

    //The list of active entities in this manager
    List<Entity> activeEntities;

    //The list of systems that runs on this manager
    List<System> systems;

    //TODO entity pool
    //TODO component pool

    /**
     * Adds a component to the componentSet
     * @param componentType the class of the component you wish to add to the pool
     * @param <T> The type of the component, must extend {@link Component}
     */
    public <T extends Component> void createComponent(Class<T> componentType) {
        try {
            componentSet.add(componentType.getDeclaredConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Log.crash("Could not Create Component", new RuntimeException(e));
        }
    }

    /**
     * Gets a component of the type requested from the componentSet
     * @param type the class of the component you wish to retrieve
     * @param <T> any component which extends {@link Component}
     * @return a component object from the componentSet of the type requested.
     */
    public <T extends Component> T getComponent(Class<T> type) {
        List<T> list = componentSet.stream().filter(type::isInstance).map(component -> (T) component).toList();
        if (list.size() > 1) Log.warn("Multiple components of same type exist in component collection");
        if (list.size() == 1) return list.get(0); //TODO init the component with default values
        return null;
    }

    /**
     * Updates all of the systems
     */
    public void update() {
        systems.forEach(system -> {
            //TODO run the system
        });
    }
}