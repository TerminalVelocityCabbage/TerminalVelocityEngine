package com.terminalvelocitycabbage.engine.pooling;

import com.terminalvelocitycabbage.engine.utils.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionPool<T> extends TypePool<T> {
    
    Constructor constructor;

    public ReflectionPool(Class<T> type, int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
        setConstructorOrError(type);
    }

    public ReflectionPool(Class<T> type, int initialCapacity) {
        super(initialCapacity);
        setConstructorOrError(type);
    }

    public ReflectionPool(Class<T> type) {
        super();
        setConstructorOrError(type);
    }

    private void setConstructorOrError(Class<T> type) {
        constructor = ClassUtils.findConstructor(type);
        if (constructor == null) throw new RuntimeException("Could not find no-arg constructor for type: " + type.getName());
    }

    @Override
    protected T createObject() {
        try {
            return (T) constructor.newInstance((Object[])null);
        } catch (Exception e) {
            throw new RuntimeException("Could not create instance of: " + constructor.getDeclaringClass().getName(), e);
        }
    }
}
