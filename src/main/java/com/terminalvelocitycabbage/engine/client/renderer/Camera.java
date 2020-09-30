package com.terminalvelocitycabbage.engine.client.renderer;

import org.joml.Matrix4f;

public class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;
	private Matrix4f projectionMatrix;

	public Camera(float fov, float clippingPlane, float farPlane, int width, int height) {
		this.fov = fov;
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;
		this.projectionMatrix = updateProjectionMatrix(width, height);
	}

	public Matrix4f updateProjectionMatrix(int width, int height) {
		return new Matrix4f().perspective(fov, (float)width/height, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
