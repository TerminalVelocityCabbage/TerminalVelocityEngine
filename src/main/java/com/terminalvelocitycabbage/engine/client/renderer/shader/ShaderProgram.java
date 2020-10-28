package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.renderer.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.components.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	private final int id;
	private boolean enabled = false;
	private static final List<Shader> shaderQueue = new ArrayList<>();
	private final Map<String, Integer> uniforms;

	public ShaderProgram() {
		this.id = glCreateProgram();
		if (this.id == 0) {
			throw new RuntimeException("Could not create shader program");
		}
		uniforms = new HashMap<>();
	}

	public void queueShader(int type, ResourceManager resourceManager, Identifier identifier) {
		shaderQueue.add(new Shader(type, id, resourceManager, identifier));
	}

	public void build() {
		for (Shader shader : shaderQueue) {
			shader.create();
		}
		glLinkProgram(id);
		if (glGetProgrami(id, GL_LINK_STATUS) == GL_FALSE) {
			Log.error(glGetProgramInfoLog(id));
		}
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

	public void createMaterialUniform(String name) {
		createUniform(name + ".ambient");
		createUniform(name + ".diffuse");
		createUniform(name + ".specular");
		createUniform(name + ".hasTexture");
		createUniform(name + ".reflectivity");
	}

	public void createPointLightUniform(String name) {
		createUniform(name + ".color");
		createUniform(name + ".position");
		createUniform(name + ".intensity");
		createUniform(name + ".attenuation.constant");
		createUniform(name + ".attenuation.linear");
		createUniform(name + ".attenuation.exponential");
	}

	public void createSpotLightUniform(String name) {
		createPointLightUniform(name + ".pointLight");
		createUniform(name + ".coneDirection");
		createUniform(name + ".cutoff");
	}

	public void createDirectionalLightUniform(String name) {
		createUniform(name + ".direction");
		createUniform(name + ".color");
		createUniform(name + ".intensity");
	}

	public void setUniform(String name, int value) {
		test();
		glUniform1i(uniforms.get(name), value);
	}

	public void setUniform(String name, float value) {
		test();
		glUniform1f(uniforms.get(name), value);
	}

	public void setUniform(String name, Vector3f value) {
		test();
		glUniform3f(uniforms.get(name), value.x, value.y, value.z);
	}

	public void setUniform(String name, Vector4f value) {
		test();
		glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
	}

	public void setUniform(String name, Matrix4f value) {
		test();
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = stack.mallocFloat(16);
			value.get(fb);
			glUniformMatrix4fv(uniforms.get(name), false, fb);
		}
	}

	public void setUniform(String name, PointLight pointLight) {
		setUniform(name + ".color", pointLight.getColor());
		setUniform(name + ".position", pointLight.getPosition());
		setUniform(name + ".intensity", pointLight.getIntensity());
		Attenuation att = pointLight.getAttenuation();
		setUniform(name + ".attenuation.constant", att.getConstant());
		setUniform(name + ".attenuation.linear", att.getLinear());
		setUniform(name + ".attenuation.exponential", att.getExponential());
	}

	public void setUniform(String name, SpotLight spotLight) {
		setUniform(name + ".pointLight", (PointLight)spotLight);
		setUniform(name + ".coneDirection", spotLight.getConeDirection());
		setUniform(name + ".cutoff", spotLight.getCutoff());
	}

	public void setUniform(String name, DirectionalLight directionalLight) {
		setUniform(name + ".direction", directionalLight.getDirection());
		setUniform(name + ".color", directionalLight.getColor());
		setUniform(name + ".intensity", directionalLight.getIntensity());
	}

	public void setUniform(String name, Material material) {
		setUniform(name + ".ambient", material.getAmbientColor());
		setUniform(name + ".diffuse", material.getDiffuseColor());
		setUniform(name + ".specular", material.getSpecularColor());
		setUniform(name + ".hasTexture", material.hasTexture() ? 1 : 0);
		setUniform(name + ".reflectivity", material.getReflectivity());
	}

	private void test() {
		if (!enabled) {
			throw new RuntimeException("Cant update uniform value before shader program is enabled.");
		}
	}

	public void enable() {
		glUseProgram(id);
		enabled = true;
	}

	public void disable() {
		glUseProgram(0);
		enabled = false;
	}

	public void delete() {
		glDeleteProgram(id);
		enabled = false;
	}

	public int getID() {
		return id;
	}
}
