package com.terminalvelocitycabbage.engine.client.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

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

	public int getType() {
		return type;
	}

	public int getShaderProgram() {
		return shaderProgram;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public Identifier getIdentifier() {
		return identifier;
	}
}
