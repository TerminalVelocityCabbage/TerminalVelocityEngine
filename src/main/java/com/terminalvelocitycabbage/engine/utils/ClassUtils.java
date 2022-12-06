package com.terminalvelocitycabbage.engine.utils;

import javax.management.ReflectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassUtils {

	public static Method[] getAllMethodsInHierarchy(Class<?> objectClass) {
		Set<Method> allMethods = new HashSet<>(Arrays.asList(objectClass.getDeclaredMethods()));
		if (objectClass.getSuperclass() != null) {
			allMethods.addAll(Arrays.asList(ClassUtils.getAllMethodsInHierarchy(objectClass.getSuperclass())));
		}
		return allMethods.toArray(new Method[0]);
	}

	public static <T> T createInstance(Class<T> clazz) throws ReflectionException {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			throw new ReflectionException(e, "Could not instantiate instance of class: " + clazz.getName());
		}
	}

}
