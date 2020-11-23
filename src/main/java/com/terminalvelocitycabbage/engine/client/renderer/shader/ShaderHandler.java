package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderHandler {

	private Map<String, ShaderProgram> programs = new HashMap<>();

	private Runnable duplicateError(String name) {
		Log.error("Program of name " + name + " already exists in this shader handler.");
		return null;
	}

	public void newProgram(String name) {
		if (programs.containsKey(name)) {
			duplicateError(name);
		} else {
			programs.put(name, new ShaderProgram());
		}
	}

	public ShaderProgram get(String name) {
		if (programs.containsKey(name)) {
			return programs.get(name);
		} else {
			throw new RuntimeException("No shader program defined with id " + name);
		}
	}

	public void queueShader(String name, Shader.Type type, ResourceManager resourceManager, Identifier identifier) {
		programs.get(name).queueShader(type.getGLType(), resourceManager, identifier);
	}

	public void build(String name) {
		programs.get(name).build();
	}

	public void enable(String name) {
		programs.get(name).enable();
	}

	public void disable(String name) {
		programs.get(name).disable();
	}

	public void delete(String name) {
		programs.get(name).delete();
	}

	public void cleanup() {
		glUseProgram(0);
		for (ShaderProgram program : programs.values()) {
			program.delete();
		}
	}
}
