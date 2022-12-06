package com.terminalvelocitycabbage.engine.ecs.pools;

import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.pooling.Pool;

public class EntityPool extends Pool<Entity> {

    /**
     * @param initialCapacity The number of objects wanted to be initialized in the freeObjects array
     * @param maxCapacity     the maximum number of objects allowed in this pool
     */
    public EntityPool(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    @Override
    protected Entity createObject() {
        return new Entity();
    }
}
