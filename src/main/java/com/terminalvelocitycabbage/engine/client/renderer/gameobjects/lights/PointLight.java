package com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.EmptyGameObject;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointLight extends EmptyGameObject {

	private Vector4f color;
	protected float intensity;
	private Attenuation attenuation;

	public PointLight(Vector3f position, Vector3f color, float intensity, Attenuation attenuation) {
		this.position = position;
		this.color = new Vector4f(color, 1.0f);
		if (intensity > 1 || intensity < 0) {
			Log.warn("Intensity of a light should be from 0 to 1, one or more light has been capped.");
			intensity = intensity > 1 ? 1 : 0;
		}
		this.intensity = intensity;
		this.attenuation = attenuation;
	}

	public PointLight(Vector3f position, Vector3f color, float intensity) {
		this(position, color, intensity, new Attenuation(1, 0, 0));
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = new Vector4f(color, 1.0f);
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

	public void update(Matrix4f viewMatrix) {
		Vector4f newPos = new Vector4f(position, 1.0f);
		newPos.mul(viewMatrix);
	}

	public void setAttenuation(Attenuation attenuation) {
		this.attenuation = attenuation;
	}

	@Override
	public void update() {
	}
}
