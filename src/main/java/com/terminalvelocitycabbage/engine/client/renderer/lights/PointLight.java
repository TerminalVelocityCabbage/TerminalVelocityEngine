package com.terminalvelocitycabbage.engine.client.renderer.lights;

import com.terminalvelocitycabbage.engine.client.renderer.lights.components.Attenuation;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLight {

	private Vector3f position;
	private Vector3f color;
	protected float intensity;
	private Attenuation attenuation;

	public PointLight(Vector3f position, Vector3f color, float intensity, Attenuation attenuation) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
		this.attenuation = attenuation;
	}

	public PointLight(Vector3f position, Vector3f color, float intensity) {
		this(position, color, intensity, new Attenuation(1, 0, 0));
	}

	//Used for copying a point light
	public PointLight(PointLight pointLight, Vector4f position) {
		this(new Vector3f(pointLight.color), new Vector3f(position.x, position.y, position.z), pointLight.intensity, pointLight.attenuation);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Attenuation getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}
}
