package com.terminalvelocitycabbage.engine.client.renderer.lights;

import org.joml.Vector3f;
import org.joml.Vector4f;

public class DirectionalLight {

	Vector3f direction; //TODO replace with rotation component
	Vector4f color;
	float intensity;

	public DirectionalLight(Vector3f direction, Vector4f color, float intensity) {
		this.direction = direction;
		this.color = color;
		this.intensity = intensity;
	}

	public Vector3f getDirection() {
		return direction;
	}

	public DirectionalLight setDirection(Vector3f direction) {
		this.direction = direction;
		return this;
	}

	public Vector4f getColor() {
		return color;
	}

	public DirectionalLight setColor(Vector4f color) {
		this.color = color;
		return this;
	}

	public float getIntensity() {
		return intensity;
	}

	public DirectionalLight setIntensity(float intensity) {
		this.intensity = intensity;
		return this;
	}
}
