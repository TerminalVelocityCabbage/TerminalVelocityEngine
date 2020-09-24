package com.terminalvelocitycabbage.engine.renderutils;

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

	public static List<Shader> shaderQueue = new ArrayList<>();

	public ShaderHandler() {
	}

	public static void queueShader(int type, int shaderProgram, ResourceManager resourceManager, Identifier identifier) {
		shaderQueue.add(new Shader(type, shaderProgram, resourceManager, identifier));
	}

	private static void createShader(Shader shader) {
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

	public static void setupShaders(int shaderProgram) {
		for (Shader shader : shaderQueue) {
			createShader(shader);
		}
		glLinkProgram(shaderProgram);
		if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
			Log.error(glGetProgramInfoLog(shaderProgram));
		}
	}
}
