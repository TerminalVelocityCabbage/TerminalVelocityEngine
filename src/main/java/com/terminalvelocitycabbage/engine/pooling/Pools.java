package com.terminalvelocitycabbage.engine.pooling;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Since {@link Pool}s are of one type, this is a way to have a whole pool for multiple types of pool, allows you to
 * search more than one pool at a time.
 */
public class Pools {

    //The list of pools mapped by type
    private final Map<Class, Pool> pools = new HashMap<>();

    /**
     * @param type the type of the pool requested
     * @param <T> The type of the pool requested
     * @return the pool of the given type requested
     */
    public <T> Pool<T> get(Class<T> type) {
       return pools.get(type);
    }

    /**
     * @param type the type of the pool requested
     * @param pool the pool you want to replace the current pool of type with
     * @param <T> the type of the pool requested
     */
    public <T> void set(Class<T> type, Pool<T> pool) {
        pools.put(type, pool);
    }

    /**
     * @param type the type of the pool requested
     * @param <T> The type of the pool requested
     * @return a free object in the pool of the type specified
     */
    public <T> T obtain(Class<T> type) {
        return get(type).obtain();
    }

    /**
     * @param object the object you wish to free from one of the pools in pools
     */
    public void free(Object object) {
        Pool pool = pools.get(object.getClass());
        pool.free(object);
    }

    /**
     * @param objects the objects you with to free from one or many of the pools in pools
     */
    public void free(Object... objects) {
        Arrays.stream(objects).toList().forEach(this::free);
    }
}