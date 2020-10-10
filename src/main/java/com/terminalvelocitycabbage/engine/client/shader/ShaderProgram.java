package com.terminalvelocitycabbage.engine.client.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	private final int id;
	private boolean inUse = false;
	private static Map<String, Shader> shaderStorage = new HashMap<>();
	private static ArrayList<String> enabledShaders = new ArrayList<>();
	private final Map<String, Integer> uniforms;

	public ShaderProgram() {
		this.id = glCreateProgram();
		if (this.id == 0) {
			throw new RuntimeException("Could not create shader program");
		}
		uniforms = new HashMap<>();
	}

	public void createShader(String name, int type, ResourceManager resourceManager, Identifier identifier) {
		shaderStorage.put(name, new Shader(type, id, resourceManager, identifier).bind());
	}

	public void enableShader(String name) {
		getShader(name).attach();
	}

	public void disableShader(String name) {
		getShader(name).detach();
	}

	private Shader getShader(String name) {
		if (!shaderStorage.containsKey(name)) {
			throw new RuntimeException("Could not get shader " + name + ", it doesnt exist");
		}
		return shaderStorage.get(name);
	}

	public void createUniform(String name) {
		int uniformLocation = glGetUniformLocation(id, name);
		if (uniformLocation < 0) {
			//Note: when a uniform is not used in a shader it will silently be removed upon compilation
			//		this can lead to some confusion here since it may be defined in the shader correctly,
			//		but if it's not used it will also throw this error.
			throw new RuntimeException("No uniform defined by name: " + name);
		}
		uniforms.put(name, uniformLocation);
	}

	public void setUniformMat4f(String name, Matrix4f value) {
		if (!inUse) {
			throw new RuntimeException("Cant update uniform value before shader program is in use.");
		}
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}

	public void link() {
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			Log.error(glGetProgramInfoLog(id));
		}
	}

	public void use() {
		glUseProgram(id);
		inUse = true;
	}

	public void delete() {
		for (Shader shader : shaderStorage.values()) {
			shader.detach();
			shader.delete();
		}
		glDeleteProgram(id);
		inUse = false;
	}
}
