package com.terminalvelocitycabbage.engine.events;

import com.terminalvelocitycabbage.engine.utils.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

public class EventDispatcher {

	private static final HashMap<EventContext, EventHandler> handlers = new HashMap<>();

	public EventDispatcher() {
		for (EventContext context: EventContext.values()) {
			handlers.put(context, new EventHandler(context));
		}
	}

	public void addEventHandler(EventContext type, Object listener) {
		handlers.get(type).addListener(listener);
	}

	public void removeEventHandler(Object listener) {
		for (EventHandler handler : handlers.values()) {
			handler.removeListener(listener);
		}
	}

	public void dispatchEvent(Event event) {
		for (Object listener : handlers.get(event.getContext()).getListeners()) {
			dispatchEventTo(event, listener);
		}
	}

	protected void dispatchEventTo(Event event, Object listener) {
		Collection<Method> methods = findMatchingEventHandlerMethods(listener, event.getName());
		for (Method method : methods) {
			try {
				// Workaround for a JDK bug:
				method.setAccessible(true);

				if (method.getParameterTypes().length == 0)
					method.invoke(listener);
				if (method.getParameterTypes().length == 1)
					method.invoke(listener, event);
				if (method.getParameterTypes().length == 2)
					method.invoke(listener, this, event);
			} catch (Exception e) {
				System.err.println("Could not invoke event handler!");
				e.printStackTrace(System.err);
			}
		}
	}

	protected Collection<Method> findMatchingEventHandlerMethods(Object handler, String eventName) {
		Method[] methods = ClassUtils.getAllMethodsInHierarchy(handler.getClass());
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
