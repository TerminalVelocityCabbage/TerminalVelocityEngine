package com.terminalvelocitycabbage.engine.pooling;

import java.util.ArrayList;
import java.util.Arrays;
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
     * Creates a new object to represent the type of item in this pool
     * Needs to be implemented when this pool is created
     * @return a new instance of the type of object that this pool contains
     */
    abstract protected T createObject();

    /**
     * Creates a number of free objects in the freeObjects pool
     * @param quantity the number of objects to be created in this pool
     */
    public void create(int quantity) {
        for (int i = 0; i < quantity; i++) {
            if (freeObjects.size() < maxObjects) {
                freeObjects.add(createObject());
            }
        }
    }

    /**
     * Resets a poolable item to its defaults so that it can be used right away when obtained from this pool
     * @param item The item that is to be reset
     */
    public void reset(T item) {
        if (item instanceof Poolable) ((Poolable)item).reset();
    }

    /**
     * Clears the object pool of al free objects
     */
    public void clear() {
        freeObjects.clear();
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
     * resets the item, and adds it to the free objects pool
     * @param item the item you wish to free
     */
    public void free(T item) {
        if (item == null) return;
        if (freeObjects.size() < maxObjects) {
            reset(item);
            freeObjects.add(item);
        }
    }

    /**
     * frees the specified items for reuse in this pool
     * @param items the items you wish to free
     */
    public void free(T... items) {
        Arrays.stream(items).forEach(this::free);
    }
}
