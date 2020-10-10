package com.terminalvelocitycabbage.engine.client.renderer.components;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;

	private final Vector3f position;
	private final Vector3f rotation;

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;

		projectionMatrix = updateProjectionMatrix(Renderer.getWindow().width(), Renderer.getWindow().height());
		viewMatrix = new Matrix4f();

		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void move(float offsetX, float offsetY, float offsetZ) {
		if ( offsetZ != 0 ) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
			position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if ( offsetX != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
			position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		position.y += offsetY;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		rotation.x = x;
		rotation.y = y;
		rotation.z = z;
	}

	public void rotate(float roll, float pitch, float yaw) {
		rotation.x += roll;
		rotation.y += pitch;
		rotation.z += yaw;
	}

	public Matrix4f updateProjectionMatrix(int width, int height) {
		return new Matrix4f().perspective(fov, (float)width/height, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		//First do the rotation so camera rotates over its position instead of world position
		viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
				.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
				.rotate((float)Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
		//Then do the translation in opposite direction of the camera movement
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
}
