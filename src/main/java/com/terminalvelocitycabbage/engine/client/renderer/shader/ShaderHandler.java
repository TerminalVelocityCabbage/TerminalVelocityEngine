package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.glUseProgram;

public class ShaderHandler {

	private Map<String, ShaderProgram> programs = new HashMap<>();
	private Map<String, UniformBufferObject> ubos = new HashMap<>();

	public ShaderProgram newProgram(String name) {
		if (programs.containsKey(name)) {
			throw new IllegalArgumentException("Program of name " + name + " already exists in this shader handler.");
		}
		ShaderProgram program = new ShaderProgram(name);
		programs.put(name, program);
		return program;
	}

	public ShaderProgram get(String name) {
		if (!programs.containsKey(name)) {
			Log.crash("Shader Retrieval Error", "undefined shader", new RuntimeException("No shader program defined with id " + name));
		}
		return programs.get(name);
	}

	@Deprecated
	//Reason: Moved to ShaderProgram Method Call. This method delegates to it but is marked to be removed
	public void queueShader(String name, Shader.Type type, ResourceManager resourceManager, Identifier identifier) {
		programs.get(name).queueShader(type.getGLType(), resourceManager, identifier);
	}

	@Deprecated
	//Reason: Moved to ShaderProgram Method Call. This method delegates to it but is marked to be removed
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
