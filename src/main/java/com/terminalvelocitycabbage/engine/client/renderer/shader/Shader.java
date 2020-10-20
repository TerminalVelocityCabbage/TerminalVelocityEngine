package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glAttachShader;

public class Shader {

	private final int type;
	private final int shaderProgram;
	private final ResourceManager resourceManager;
	private final Identifier identifier;

	Shader(int type, int shaderProgram, ResourceManager resourceManager, Identifier identifier) {
		this.type = type;
		this.shaderProgram = shaderProgram;
		this.resourceManager = resourceManager;
		this.identifier = identifier;
	}

	public void create() {
		Optional<Resource> resource = resourceManager.getResource(identifier);
		String src = resource.flatMap(Resource::asString).orElseThrow();
		int shader = glCreateShader(type);
		glShaderSource(shader, src);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Could not compile shader " + identifier.getPath() + " " + glGetShaderInfoLog(shader));
		}
		glAttachShader(shaderProgram, shader);
		glDeleteShader(shader);
	}
}
