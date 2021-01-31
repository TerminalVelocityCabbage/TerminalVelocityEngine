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
	private final Vector3f tempVec3;
	private final Quaternionf rotation;

	public Camera(int fov, float clippingPlane, float farPlane) {
		this.fov = (float)Math.toRadians(fov);
		this.clippingPlane = clippingPlane;
		this.farPlane = farPlane;

		projectionMatrix = createProjectionMatrix(Renderer.getWindow().aspectRatio());
		viewMatrix = new Matrix4f();

		position = new Vector3f(0, 0, 0);
		tempVec3 = new Vector3f();
		rotation = new Quaternionf();
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(float x, float y, float z) {
		position.set(x, y, z);
	}

	public void move(float offsetX, float offsetY, float offsetZ) {
		position.add(
				((float)Math.sin(rotation.y) * -offsetZ) + ((float)Math.sin(rotation.y - 90) * -offsetX),
				offsetY,
				((float)Math.cos(rotation.y) * offsetZ) + ((float)Math.cos(rotation.y - 90) * offsetX));
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

	private Matrix4f createProjectionMatrix(float aspectRatio) {
		return new Matrix4f().perspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public void updateProjectionMatrix(float aspectRatio) {
		this.projectionMatrix = projectionMatrix.setPerspective(fov, aspectRatio, clippingPlane, farPlane);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		viewMatrix.rotation(rotation);
		viewMatrix.translate(position.negate(tempVec3));
		return viewMatrix;
	}
}
