package com.terminalvelocitycabbage.engine.client.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL20.*;

public class ShaderHandler {

	private final int shaderProgram;
	private static final List<Shader> shaderQueue = new ArrayList<>();
	private final Map<String, Integer> uniforms;

	public ShaderHandler() {
		this.shaderProgram = glCreateProgram();
		uniforms = new HashMap<>();
	}

	public void queueShader(int type, ResourceManager resourceManager, Identifier identifier) {
		shaderQueue.add(new Shader(type, shaderProgram, resourceManager, identifier));
	}

	private void createShader(Shader shader) {
		Optional<Resource> resource = shader.getResourceManager().getResource(shader.getIdentifier());
		String src = resource.flatMap(Resource::asString).orElseThrow();
		int typedShader = glCreateShader(shader.getType());
		glShaderSource(typedShader, src);
		glCompileShader(typedShader);
		if (glGetShaderi(typedShader, GL_COMPILE_STATUS) == GL_FALSE) {
			Log.error(glGetShaderInfoLog(shader.getShaderProgram()));
		}
		glAttachShader(shader.getShaderProgram(), typedShader);
		glDeleteShader(typedShader);
	}

	public void createUniform(String name) {
		int uniformLocation = glGetUniformLocation(shaderProgram, name);
		if (uniformLocation < 0) {
			throw new RuntimeException("No uniform defined by name: " + name);
		}
		uniforms.put(name, shaderProgram);
	}

	public void setUniformMat4f(String name, Matrix4f value) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}

	public void bindAll() {
		for (Shader shader : shaderQueue) {
			createShader(shader);
		}
		glLinkProgram(shaderProgram);
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			Log.error(glGetProgramInfoLog(shaderProgram));
		}
	}

	public void use() {
		glUseProgram(shaderProgram);
	}

	public void delete() {
		glDeleteProgram(shaderProgram);
	}
}
