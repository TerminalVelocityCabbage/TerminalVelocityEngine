package com.terminalvelocitycabbage.engine.client.renderer.lights;

import com.terminalvelocitycabbage.engine.utils.Color;
import org.joml.Vector3f;

public class DirectionalLight {

	Vector3f direction; //TODO replace with rotation component
	Color color;
	float intensity;

	public DirectionalLight(Vector3f direction, Color color, float intensity) {
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

	public Color getColor() {
		return color;
	}

	public DirectionalLight setColor(Color color) {
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
