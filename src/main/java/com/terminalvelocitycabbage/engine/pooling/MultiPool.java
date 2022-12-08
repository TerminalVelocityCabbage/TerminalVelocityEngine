package com.terminalvelocitycabbage.engine.pooling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Since {@link TypePool}s are of one type, this is a way to have a single object to manage pools of all types with.
 */
public class MultiPool {

    //The list of pools mapped by type
    private final Map<Class, TypePool> pools = new HashMap<>();

    /**
     * @param type The type of object that this pool stores
     * @param createIfNull if true and the pool of type requested does not exist, create one and return that new pool
     * @param initialCountIfCreated if a new pool is created initialize that pool with this number of free objects
     * @param <T> The type of object that this pool stores
     * @return the pool that stores the type requested
     */
    @SuppressWarnings("unchecked")
    public <T extends Poolable> TypePool<T> getPool(Class<T> type, boolean createIfNull, int initialCountIfCreated) {
        if (!pools.containsKey(type) && createIfNull) {
            createPool(type, new ReflectionPool<>(type), initialCountIfCreated);
        }
        return getPool(type);
    }

    /**
     * @param type The type of object that this pool stores
     * @param createIfNull if true and the pool of type requested does not exist, create one and return that new pool
     * @param <T> The type of object that this pool stores
     * @return the pool that stores the type requested
     */
    @SuppressWarnings("unchecked")
    public <T extends Poolable> TypePool<T> getPool(Class<T> type, boolean createIfNull) {
        if (!pools.containsKey(type) && createIfNull) {
            createPool(type, new ReflectionPool<>(type));
        }
        return getPool(type);
    }

    /**
     * @param type the type of the pool requested
     * @param <T> The type of the pool requested
     * @return the pool of the given type requested
     */
    @SuppressWarnings("unchecked")
    public <T extends Poolable> TypePool<T> getPool(Class<T> type) {
       return pools.get(type);
    }

    /**
     * @param type the type of the pool requested
     * @param pool the pool you want to replace the current pool of type with
     * @param <T> the type of the pool requested
     */
    public <T extends Poolable> void createPool(Class<T> type, TypePool<T> pool) {
        pools.put(type, pool);
    }

    /**
     * @param type the type of the pool requested
     * @param pool the pool you want to replace the current pool of type with
     * @param initialCount The number of objects you want to fill this pool with initially
     * @param <T> the type of the pool requested
     */
    public <T extends Poolable> void createPool(Class<T> type, TypePool<T> pool, int initialCount) {
        pool.fill(initialCount);
        pools.put(type, pool);
    }

    /**
     * @param type the type of the pool requested
     * @param <T> The type of the pool requested
     * @return a free object in the pool of the type specified
     */
    public <T extends Poolable> T obtain(Class<T> type) {
        return getPool(type).obtain();
    }

    /**
     * @param object the object you wish to free from one of the pools in pools
     */
    public void free(Poolable object) {
        TypePool pool = pools.get(object.getClass());
        pool.free(object);
    }

    /**
     * @param objects the objects you with to free from one or many of the pools in pools
     */
    public void free(Object... objects) {
        Arrays.stream(objects).toList().forEach(this::free);
    }
}
