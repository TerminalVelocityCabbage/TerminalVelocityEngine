package com.terminalvelocitycabbage.engine.client.renderer.shader;

import com.terminalvelocitycabbage.engine.client.renderer.lights.Attenuation;
import com.terminalvelocitycabbage.engine.client.renderer.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.PointLight;
import com.terminalvelocitycabbage.engine.client.renderer.lights.SpotLight;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.debug.Log;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.utils.Color;
import com.terminalvelocitycabbage.engine.utils.Pair;
import org.joml.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.FRAGMENT;
import static com.terminalvelocitycabbage.engine.client.renderer.shader.Shader.Type.VERTEX;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

	private final int id;
	private boolean enabled = false;
	private static final List<Shader> shaderQueue = new ArrayList<>();
	private final Map<String, Integer> uniforms;

	public static final int MAX_POINT_LIGHTS = 256;
	public static final int MAX_SPOT_LIGHTS = 256;

	private final String name;

	public ShaderProgram(String name) {
		this.id = glCreateProgram();
		this.name = name;
		if (this.id == 0) {
			Log.crash("Shader Program Creation Error", new RuntimeException("Could not create shader program"));
		}
		uniforms = new HashMap<>();
	}

	public ShaderProgram queueDefaultShaders(ResourceManager resourceManager, String namespace) {
		return this
			.queueShader(VERTEX, resourceManager, new Identifier(namespace, this.name + ".vert"))
			.queueShader(FRAGMENT, resourceManager, new Identifier(namespace, this.name + ".frag"));
	}

	public ShaderProgram queueShader(Shader.Type type, ResourceManager resourceManager, Identifier identifier, Pair<String, String>... defines) {
		return this.queueShader(type.getGLType(), resourceManager, identifier, defines);
	}

	public ShaderProgram queueShader(int type, ResourceManager resourceManager, Identifier identifier, Pair<String, String>... defines) {
		shaderQueue.add(new Shader(type, id, resourceManager, identifier, defines));
		return this;
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
//			throw new RuntimeException("No uniform defined by name: " + name);
			return;
		}
		uniforms.put(name, uniformLocation);
	}

	private int getUniformId(String name) {
		return glGetUniformLocation(id, name);
	}

	public void createMaterialUniform(String name) {
		createUniform(name + ".ambient");
		createUniform(name + ".diffuse");
		createUniform(name + ".specular");
		createUniform(name + ".hasTexture");
		createUniform(name + ".hasTexturedReflectivity");
		createUniform(name + ".reflectivity");
	}

	private void createPointLightUniform(String name) {
		createUniform(name + ".color");
		createUniform(name + ".position");
		createUniform(name + ".intensity");
		createUniform(name + ".attenuation.constant");
		createUniform(name + ".attenuation.linear");
		createUniform(name + ".attenuation.exponential");
	}

	public void createPointLightUniforms(String name, int numPointLights) {
		createUniform(name + "Num");
		if (numPointLights > MAX_POINT_LIGHTS) {
			throw new RuntimeException(numPointLights + " is greater than the maximum allowed number of point lights in TVE of " + MAX_POINT_LIGHTS);
		}
		for (int i = 0; i < numPointLights; i++) {
			createPointLightUniform(name + "[" + i + "]");
		}
	}

	private void createSpotLightUniform(String name) {
		createUniform(name + ".color");
		createUniform(name + ".position");
		createUniform(name + ".intensity");
		createUniform(name + ".attenuation.constant");
		createUniform(name + ".attenuation.linear");
		createUniform(name + ".attenuation.exponential");
		createUniform(name + ".coneDirection");
		createUniform(name + ".cutoff");
	}

	public void createSpotLightUniforms(String name, int numSpotLights) {
		createUniform(name + "Num");
		if (numSpotLights > MAX_SPOT_LIGHTS) {
			throw new RuntimeException(numSpotLights + " is greater than the maximum allowed number of spot lights in TVE of " + MAX_SPOT_LIGHTS);
		}
		for (int i = 0; i < numSpotLights; i++) {
			createSpotLightUniform(name + "[" + i + "]");
		}
	}

	public void createDirectionalLightUniform(String name) {
		createUniform(name + ".direction");
		createUniform(name + ".color");
		createUniform(name + ".intensity");
	}

	public void setUniform(String name, int value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform1i(id, value);
		}
	}

	public void setUniform(String name, float value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform1f(id, value);
		}
	}

	public void setUniformArray(String name, float floatV, int position) {
		setUniform(name + "[" + position + "]", floatV);
	}

	public void setUniformArray(String name, float[] floats) {
		for (int i = 0; i < (floats != null ? floats.length : 0); i++) {
			setUniformArray(name, floats[i], i);
		}
	}

	public void setUniform(String name, Vector2f value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform2f(id, value.x, value.y);
		}
	}

	public void setUniform(String name, float x, float y) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform2f(id, x, y);
		}
	}

	public void setUniform(String name, Vector3f value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform3f(id, value.x, value.y, value.z);
		}
	}

	public void setUniform(String name, float x, float y, float z) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform3f(id, x, y, z);
		}
	}

	public void setUniform(String name, Vector4f value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform4f(id, value.x, value.y, value.z, value.w);
		}
	}

	public void setUniform(String name, Color value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform4f(id, value.r(), value.g(), value.b(), value.a());
		}
	}

	public void setUniform(String name, float x, float y, float z, float w) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			glUniform4f(id, x, y, z, w);
		}
	}

	public void setUniform(String name, Matrix3f value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer fb = stack.mallocFloat(9);
				value.get(fb);
				glUniformMatrix3fv(id, false, fb);
			}
		}
	}

	public void setUniform(String name, Matrix4f value) {
		test();
		int id = uniforms.computeIfAbsent(name, this::getUniformId);
		if(id != -1) {
			try (MemoryStack stack = MemoryStack.stackPush()) {
				FloatBuffer fb = stack.mallocFloat(16);
				value.get(fb);
				glUniformMatrix4fv(id, false, fb);
			}
		}
	}

	//Should only be used by spot light setting point light unforms or by the actual point light setter that accounts for position
	private void setUniform(String name, PointLight pointLight) {
		setUniform(name + ".color", pointLight.getColor());
		setUniform(name + ".position", pointLight.getPosition());
		setUniform(name + ".intensity", pointLight.getIntensity());
		Attenuation att = pointLight.getAttenuation();
		setUniform(name + ".attenuation.constant", att.getConstant());
		setUniform(name + ".attenuation.linear", att.getLinear());
		setUniform(name + ".attenuation.exponential", att.getExponential());
	}

	public void setUniform(String name, PointLight pointLight, int position) {
		setUniform(name + "[" + position + "]", pointLight);
	}

	public void setUniforms(String name, PointLight[] pointLights) {
		for (int i = 0; i < (pointLights != null ? pointLights.length : 0); i++) {
			setUniform(name, pointLights[i], i);
		}
	}

	private void setUniform(String name, SpotLight spotLight) {
		setUniform(name + ".color", spotLight.getColor());
		setUniform(name + ".position", spotLight.getPosition());
		setUniform(name + ".intensity", spotLight.getIntensity());
		Attenuation att = spotLight.getAttenuation();
		setUniform(name + ".attenuation.constant", att.getConstant());
		setUniform(name + ".attenuation.linear", att.getLinear());
		setUniform(name + ".attenuation.exponential", att.getExponential());
		setUniform(name + ".coneDirection", spotLight.getConeDirection());
		setUniform(name + ".cutoff", spotLight.getCutoff());
	}

	public void setUniform(String name, SpotLight spotLight, int position) {
		setUniform(name + "[" + position + "]", spotLight);
	}

	public void setUniform(String name, SpotLight[] spotLights) {
		for (int i = 0; i < (spotLights != null ? spotLights.length : 0); i++) {
			setUniform(name, spotLights[i], i);
		}
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
		setUniform(name + ".hasTexturedReflectivity", material.hasReflectivityTexture() ? 1 : 0);
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
