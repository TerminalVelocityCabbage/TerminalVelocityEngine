package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Vector4f;

public class Material {

	public static final Vector4f DEFAULT_ALBEDO_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

	//if the texture exists the colors below will be ignored by the shader.
	private Texture texture;
	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;

	//One of these will be null
	private float reflectivity;
	private Texture reflectivityTexture;

	public static Builder builder() {
		return new Builder();
	}

	private Material(Texture albedo, Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, float reflectivity, Texture reflectivityTexture) {
		this.texture = albedo;
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.reflectivity = reflectivity;
		this.reflectivityTexture = reflectivityTexture;
	}

	public static class Builder {

		//The normal color values of the texture
		private Texture albedoTexture;
		//A texture with only alpha channels determining reflectivity at a pixel
		private Texture reflectivityTexture;

		//If the albedo texture isn't set there needs to be some way of determining colors
		private Vector4f ambientColor;
		private Vector4f diffuseColor;
		private Vector4f specularColor;
		//If a reflectivity texture isn't present
		private float reflectivity;

		public Builder texture(Texture texture) {
			this.albedoTexture = texture;
			return this;
		}

		public Builder reflectivity(Texture texture) {
			this.reflectivityTexture = texture;
			return this;
		}

		public Builder reflectivity(float reflectivity) {
			this.reflectivity = reflectivity;
			return this;
		}

		public Builder color(float r, float g, float b, float a) {
			this.ambientColor = new Vector4f(r, g, b, a);
			return this;
		}

		public Builder ambientColor(float r, float g, float b, float a) {
			this.ambientColor = new Vector4f(r, g, b, a);
			return this;
		}

		public Builder diffuseColor(float r, float g, float b, float a) {
			this.diffuseColor = new Vector4f(r, g, b, a);
			return this;
		}

		public Builder specularColor(float r, float g, float b, float a) {
			this.specularColor = new Vector4f(r, g, b, a);
			return this;
		}

		public Material build() {
			//Check colors
			if (ambientColor == null) ambientColor = DEFAULT_ALBEDO_COLOR;
			if (diffuseColor == null) diffuseColor = ambientColor;
			if (specularColor == null) specularColor = ambientColor;
			if (reflectivityTexture != null && albedoTexture != null && !(reflectivityTexture.height % albedoTexture.height == 0) && !(reflectivityTexture.width % albedoTexture.width == 0)) {
				throw new RuntimeException("The reflectivity texture must be the same size or a multiple of the albedo texture to be used.");
			}
			return new Material(albedoTexture, ambientColor, diffuseColor, specularColor, reflectivity, reflectivityTexture);
		}
	}

	public Texture getTexture() {
		return texture;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public Texture getReflectivityTexture() {
		return reflectivityTexture;
	}

	public boolean hasTexture() {
		return texture != null;
	}

	public boolean hasReflectivityTexture() {
		return reflectivityTexture != null;
	}

	public void setColor(float r, float g, float b, float opacity) {
		this.ambientColor.set(r, g, b, opacity);
		this.specularColor.set(r, g, b, opacity);
		this.diffuseColor.set(r, g, b, opacity);
	}
}
