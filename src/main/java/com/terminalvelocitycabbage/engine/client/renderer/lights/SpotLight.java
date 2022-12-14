package com.terminalvelocitycabbage.engine.client.renderer.lights;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class SpotLight {

	private Vector3f position;
	private Vector3f coneDirection;
	private float cutoff;
	private Vector4f color;
	protected float intensity;
	private Attenuation attenuation;

	public SpotLight(Vector3f position, Vector3f color, float intensity, Attenuation attenuation, Vector3f coneDirection, float cutoffAngle) {
		this.position = position;
		this.color = new Vector4f(color, 1.0f);
		if (intensity > 1 || intensity < 0) {
			Log.warn("Intensity of a light should be from 0 to 1, one or more light has been capped.");
			intensity = intensity > 1 ? 1 : 0;
		}
		this.intensity = intensity;
		this.attenuation = attenuation;
		this.coneDirection = coneDirection;
		this.cutoff = (float)Math.cos(Math.toRadians(cutoffAngle));
	}

	public Vector3f getConeDirection() {
		return coneDirection;
	}

	public void setConeDirection(Vector3f coneDirection) {
		this.coneDirection = coneDirection;
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}

	public void setCutoffAngle(float cutoffAngle) {
		this.setCutoff((float)Math.cos(Math.toRadians(cutoffAngle)));
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
}
