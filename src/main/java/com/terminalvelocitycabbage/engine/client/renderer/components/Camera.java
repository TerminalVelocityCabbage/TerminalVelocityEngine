package com.terminalvelocitycabbage.engine.client.renderer.components;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import org.joml.Matrix4f;

public abstract class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;

	private Matrix4f projectionMatrix = new Matrix4f();

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;
		createProjectionMatrix(Renderer.getWindow().aspectRatio());
	}

	private void createProjectionMatrix(float aspectRatio) {
		projectionMatrix.identity().perspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public void updateProjectionMatrix(float aspectRatio) {
		this.projectionMatrix.setPerspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public abstract Matrix4f getViewMatrix();
}
