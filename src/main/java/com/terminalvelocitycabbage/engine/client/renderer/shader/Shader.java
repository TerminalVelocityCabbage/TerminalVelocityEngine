package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.utils.StringUtils;

import java.util.Optional;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;

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
		while (src.contains("#include")) {
			src = parseInclusions(src);
		}
		int shader = glCreateShader(type);
		glShaderSource(shader, src);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			throw new RuntimeException("Could not compile shader " + identifier.getPath() + " " + glGetShaderInfoLog(shader));
		}
		glAttachShader(shaderProgram, shader);
		glDeleteShader(shader);
	}

	/**
	 * Parses instances of #include in a shader with shaders in the namespace provided
	 * @param source The string having inclusions parsed
	 * @return The source with inclusions inserted
	 */
	private String parseInclusions(String source) {
		//Get the name of the shader that the parent shader is trying to import
		String importName = StringUtils.getStringBetween(source, "#include \"", "\";");
		//If we dont find anything here then we just return the source
		if (importName == null) return source;

		String[] importNameSplit = importName.split(":");
		if (importNameSplit.length < 2) {
			throw new RuntimeException("You must specify a namespace and path for an imported shader");
		}
		//Try to get the resource for the shader trying to be included
		Optional<Resource> resource = resourceManager.getResource(new Identifier(importNameSplit[0], importNameSplit[1]));
		if (resource.isEmpty()) {
			throw new RuntimeException("No shader found for the inclusion " + importNameSplit[0] + ":" + importNameSplit[1]);
		}

		//Attempt to insert the included shader source in place of the requested.
		return source.replace("#include \"" + importName + "\";", resource.flatMap(Resource::asString).orElseThrow());
	}

	public enum Type {
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER),
		TESS_CONTROL(GL_TESS_CONTROL_SHADER),
		TESS_EVAL(GL_TESS_EVALUATION_SHADER);

		private final int glCode;

		private Type(int glCode) {
			this.glCode = glCode;
		}

		public int getGLType() {
			return this.glCode;
		}
	}
}
