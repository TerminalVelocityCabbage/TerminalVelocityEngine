package com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DirectionalLight extends EmptyGameObject {

	Vector3f direction;
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

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	public void rotate(float x, float y, float z) {
		this.direction.add(x, y, z);
	}

	@Override
	public void update() {
	}

	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector4f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
}
