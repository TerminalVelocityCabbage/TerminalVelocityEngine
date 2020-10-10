package com.terminalvelocitycabbage.engine.client.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class Shader {

	private final int type;
	private final int shaderProgram;
	private final ResourceManager resourceManager;
	private final Identifier identifier;

	private static int id = -1;

	Shader(int type, int shaderProgram, ResourceManager resourceManager, Identifier identifier) {
		this.type = type;
		this.shaderProgram = shaderProgram;
		this.resourceManager = resourceManager;
		this.identifier = identifier;
	}

	public static int getId() {
		return id;
	}

	public Shader bind() {
		Optional<Resource> resource = this.getResourceManager().getResource(identifier);
		String src = resource.flatMap(Resource::asString).orElseThrow();
		int shader = glCreateShader(type);
		glShaderSource(shader, src);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			Log.error("Could not compile shader " + identifier.getPath() + " " + glGetShaderInfoLog(shaderProgram));
		}
		id = shader;
		return this;
	}

	public void attach() {
		if (id == -1) {
			Log.error("Cant attach shader " + identifier.getPath() + " before it has been bound.");
		}
		glAttachShader(shaderProgram, id);
	}

	public void detach() {
		glDetachShader(shaderProgram, id);
	}

	public void delete() {
		glDeleteShader(id);
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
