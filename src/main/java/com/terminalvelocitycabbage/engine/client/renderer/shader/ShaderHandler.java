package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

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

	public UniformBufferObject newUBO(String name, UniformBufferObject ubo) {
		if (ubos.containsKey(name)) {
			throw new IllegalArgumentException("UBO of name " + name + " already exists in this shader handler.");
		}
		ubos.put(name, ubo);
		return ubo;
	}

	public ShaderProgram getShader(String name) {
		if (!programs.containsKey(name)) {
			Log.crash("Shader Retrieval Error", "undefined shader", new RuntimeException("No shader program defined with id " + name));
		}
		return programs.get(name);
	}

	public UniformBufferObject getUBO(String name) {
		if (!ubos.containsKey(name)) {
			Log.crash("UBO Retrieval Error", "undefined ubo", new RuntimeException("No ubo defined with id " + name));
		}
		return ubos.get(name);
	}

	public void cleanup() {
		glUseProgram(0);
		for (ShaderProgram program : programs.values()) {
			program.delete();
		}
		glBindBuffer(GL_UNIFORM_BUFFER, 0);
		for (UniformBufferObject ubo : ubos.values()) {
			ubo.destroy();
		}
	}
}
