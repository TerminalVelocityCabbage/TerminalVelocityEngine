package com.terminalvelocitycabbage.engine.events;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class EventDispatcher {
	Collection<Object> handlers = new ArrayList<Object>();

	public void addEventHandler(Object handler) {
		this.handlers.add(handler);
	}

	public void removeEventHandler(Object handler) {
		this.handlers.remove(handler);
	}

	public void dispatchEvent(Event event) {
		for (Object handler : handlers) {
			dispatchEventTo(event, handler);
		}
	}

	protected void dispatchEventTo(Event event, Object handler) {
		Collection<Method> methods = findMatchingEventHandlerMethods(handler, event.getName());
		for (Method method : methods) {
			try {
				// Workaround for a JDK bug:
				method.setAccessible(true);

				if (method.getParameterTypes().length == 0)
					method.invoke(handler);
				if (method.getParameterTypes().length == 1)
					method.invoke(handler, event);
				if (method.getParameterTypes().length == 2)
					method.invoke(handler, this, event);
			} catch (Exception e) {
				System.err.println("Could not invoke event handler!");
				e.printStackTrace(System.err);
			}
		}
	}

	protected Collection<Method> findMatchingEventHandlerMethods(Object handler, String eventName) {
		Method[] methods = handler.getClass().getDeclaredMethods();
		Collection<Method> result = new ArrayList<Method>();
		for (Method method : methods) {
			if (canHandleEvent(method, eventName)) {
				result.add(method);
			}
		}
		return result;
	}

	protected boolean canHandleEvent(Method method, String eventName) {
		HandleEvent handleEventAnnotation = method.getAnnotation(HandleEvent.class);
		if (handleEventAnnotation != null) {
			String[] values = handleEventAnnotation.value();
			return Arrays.asList(values).contains(eventName);
		}
		return false;
	}

}
