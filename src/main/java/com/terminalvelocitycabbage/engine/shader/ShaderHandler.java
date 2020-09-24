package com.terminalvelocitycabbage.engine.shader;

import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.Resource;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class ShaderHandler {

	private final int shaderProgram;
	private static final List<Shader> shaderQueue = new ArrayList<>();

	public ShaderHandler(int shaderProgram) {
		this.shaderProgram = shaderProgram;
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

	public void setupShaders() {
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
