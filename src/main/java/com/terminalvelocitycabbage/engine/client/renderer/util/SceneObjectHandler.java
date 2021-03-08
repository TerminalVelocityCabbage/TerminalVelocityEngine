package com.terminalvelocitycabbage.engine.client.renderer.util;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneObjectHandler {

	Map<String, EmptyGameObject> gameObjects = new HashMap<>();

	public <T extends EmptyGameObject>T add(String key, T object) {
		if (gameObjects.containsKey(key)) {
			Log.crash("Scene Object Handler Error", new RuntimeException("Added gameObject with same key to map is not allowed " + key));
			return null;
		} else {
			gameObjects.put(key, object);
			return object;
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends EmptyGameObject>List<T> getAllOfType(Class<T> type) {
		List<T> list = new ArrayList<>();
		for (EmptyGameObject gameObject : gameObjects.values()) {
			if (type.isInstance(gameObject)) {
				list.add((T) gameObject);
			}
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T extends EmptyGameObject>T getObject(String name) {
		return (T)gameObjects.get(name);
	}
}
