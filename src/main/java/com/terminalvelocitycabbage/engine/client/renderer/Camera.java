package com.terminalvelocitycabbage.engine.client.renderer;

import org.joml.Matrix4f;

public class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;
	private Matrix4f projectionMatrix;

	public Camera(float fov, float clippingPlane, float farPlane) {
		this.fov = fov;
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;
	}

}
