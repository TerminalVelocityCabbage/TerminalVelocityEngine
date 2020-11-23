package com.terminalvelocitycabbage.engine.client.renderer.lights;

import com.terminalvelocitycabbage.engine.client.renderer.lights.components.Attenuation;
import org.joml.Vector3f;

public class SpotLight extends PointLight {

	private Vector3f coneDirection;
	private float cutoff;

	public SpotLight(Vector3f position, Vector3f color, float intensity, Attenuation attenuation, Vector3f coneDirection, float cutoffAngle) {
		super(position, color, intensity, attenuation);
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
}
