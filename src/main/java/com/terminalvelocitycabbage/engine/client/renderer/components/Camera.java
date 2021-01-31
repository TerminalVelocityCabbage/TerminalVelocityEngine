package com.terminalvelocitycabbage.engine.client.renderer.components;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import org.joml.Matrix4f;

public abstract class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;

	private Matrix4f projectionMatrix;

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;

		projectionMatrix = createProjectionMatrix(Renderer.getWindow().aspectRatio());
	}

	private Matrix4f createProjectionMatrix(float aspectRatio) {
		return new Matrix4f().perspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public void updateProjectionMatrix(float aspectRatio) {
		this.projectionMatrix = projectionMatrix.setPerspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public abstract Matrix4f getViewMatrix();
}
