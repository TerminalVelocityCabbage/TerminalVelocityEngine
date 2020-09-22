package com.terminalvelocitycabbage.engine.renderutils;

import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.Resource;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;

import java.util.Optional;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDeleteShader;

public class ShaderHandler {

	public static void createShader(int type, int shaderProgram, ResourceManager resourceManager, Identifier identifier) {
		Optional<Resource> vertexShaderResource = resourceManager.getResource(identifier);
		String vertexShaderSource = vertexShaderResource.flatMap(Resource::asString).orElseThrow();
		int vertexShader = glCreateShader(type);
		glShaderSource(vertexShader, vertexShaderSource);
		glCompileShader(vertexShader);
		glAttachShader(shaderProgram, vertexShader);
		glDeleteShader(vertexShader);
	}
}
