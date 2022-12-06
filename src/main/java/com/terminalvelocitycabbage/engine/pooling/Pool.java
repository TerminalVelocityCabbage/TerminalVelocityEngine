package com.terminalvelocitycabbage.engine.pooling;

import java.util.ArrayList;
import java.util.List;

/**
 * A pool of objects that can be re-used when needed
 * @param <T> The type that this pool stores
 */
public abstract class Pool<T> {

    //The max allowed objects in this pool
    public int maxObjects;
    //The current list of available free objects
    private final List<T> freeObjects;

    /**
     * @param initialCapacity The number of objects wanted to be initialized in the freeObjects array
     * @param maxCapacity the maximum number of objects allowed in this pool
     */
    public Pool(int initialCapacity, int maxCapacity) {
        this.freeObjects = new ArrayList<>();
        maxObjects = maxCapacity;
    }

    /**
     * gets an object from this pool that is free
     * @return a free object from freeObjects in this pool
     */
    public T obtain() {
        return freeObjects.remove(freeObjects.size() - 1);
    }

    /**
     * frees the current item as not used
     * @param item the item you wish to free
     */
    public void free(T item) {
        //TODO add to free objects and reset item from Poolable interface method
    }
}
