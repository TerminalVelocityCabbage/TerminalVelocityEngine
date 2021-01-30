package com.terminalvelocitycabbage.engine.client.renderer.components;

import com.terminalvelocitycabbage.engine.client.renderer.Renderer;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {

	private float fov;
	private float clippingPlane;
	private float farPlane;

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;

	private final Vector3f position;
	private final Quaternionf rotation;

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;

		projectionMatrix = createProjectionMatrix(Renderer.getWindow().width(), Renderer.getWindow().height());
		viewMatrix = new Matrix4f();

		position = new Vector3f(0, 0, 0);
		rotation = new Quaternionf();
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

	public void move(Vector3f offset, float sensitivity) {
		move(offset.x * sensitivity, offset.y * sensitivity, offset.z * sensitivity);
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public void setRotation(float x, float y, float z) {
		rotation.rotationXYZ(x, y, z);
	}

	public void rotate(float x, float y, float z) {
		rotation.rotateXYZ(x, y, z);
	}

	public void rotate(Vector2f rotation) {
		//TODO roll option in one of these
		rotate(rotation.y, rotation.x, 0);
	}

	private Matrix4f createProjectionMatrix(int width, int height) {
		return new Matrix4f().perspective(fov, (float)width/height, clippingPlane, farPlane);
	}

	public void updateProjectionMatrix(int width, int height) {
		this.projectionMatrix = projectionMatrix.setPerspective(fov, (float)width/ height, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		viewMatrix.identity();
		//First do the rotation so camera rotates over its position instead of world position
		viewMatrix.rotateXYZ(rotation.x, rotation.y, rotation.z);
		//Then do the translation in opposite direction of the camera movement
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
}
