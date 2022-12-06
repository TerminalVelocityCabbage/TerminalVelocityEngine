package com.terminalvelocitycabbage.engine.client.renderer;

import com.terminalvelocitycabbage.engine.client.input.InputHandler;
import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;

	private Matrix4f projectionMatrix = new Matrix4f();
	protected Matrix4f viewMatrix;

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;
		createProjectionMatrix(Renderer.getWindow().aspectRatio());
		viewMatrix = new Matrix4f();
	}

	public abstract <T extends InputHandler> void update(T inputHandler, float deltaTime);

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

	public Vector3f getWorldPosition() {
		return getViewMatrix().origin(new Vector3f());
	}
}
