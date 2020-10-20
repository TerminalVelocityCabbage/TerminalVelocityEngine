package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Vector4f;

public class Material {

	public static final Vector4f DEFAULT_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

	private Vector4f ambientColor;
	private Vector4f diffuseColor;
	private Vector4f specularColor;

	private float reflectivity;

	private Texture texture;

	public Material() {
		this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, null, 0.0f);
	}

	public Material(Vector4f color, float reflectivity) {
		this(color, color, color, null, reflectivity);
	}

	public Material(Texture texture) {
		this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, 0.0f);
	}

	public Material(Texture texture, float reflectivity) {
		this(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, texture, 0.0f);
	}

	public Material(Vector4f ambientColor, Vector4f diffuseColor, Vector4f specularColor, Texture texture, float reflectivity) {
		this.ambientColor = ambientColor;
		this.diffuseColor = diffuseColor;
		this.specularColor = specularColor;
		this.texture = texture;
		this.reflectivity = reflectivity;
	}

	public Vector4f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector4f ambientColor) {
		this.ambientColor = ambientColor;
	}

	public Vector4f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector4f diffuseColor) {
		this.diffuseColor = diffuseColor;
	}

	public Vector4f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector4f specularColor) {
		this.specularColor = specularColor;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public boolean hasTexture() {
		return texture != null;
	}
}
