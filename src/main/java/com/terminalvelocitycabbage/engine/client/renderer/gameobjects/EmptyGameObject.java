package com.terminalvelocitycabbage.engine.client.renderer.gameobjects;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class EmptyGameObject {

	protected Vector3f position;
	protected Quaternionf rotation;
	protected Vector3f scale;

	protected Matrix4f transformationMatrix;

	protected boolean needsUpdate = true;
	protected boolean render = false;

	public EmptyGameObject() {
		position = new Vector3f();
		rotation = new Quaternionf();
		scale = new Vector3f(1);
		transformationMatrix = new Matrix4f();
	}

	public EmptyGameObject(Vector3f position, Quaternionf rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.transformationMatrix = new Matrix4f();
	}

	public EmptyGameObject setPosition(float x, float y, float z) {
		this.position = this.position.set(x, y, z);
		return this;
	}

	public EmptyGameObject setPosition(Vector3f newPosition) {
		this.position = newPosition;
		return this;
	}

	public EmptyGameObject move(float x, float y, float z) {
		position.add(x, y, z);
		return this;
	}

	public EmptyGameObject setRotation(float x, float y, float z) {
		this.rotation = new Quaternionf();
		rotation.rotateXYZ(x, y, z);
		return this;
	}

	public EmptyGameObject rotate(float x, float y, float z) {
		rotation.rotateXYZ(x, y, z);
		return this;
	}

	public EmptyGameObject scale(float x, float y, float z) {
		scale.add(x, y, z);
		return this;
	}

	public EmptyGameObject disable() {
		render = false;
		return this;
	}

	public EmptyGameObject enable() {
		render = true;
		return this;
	}

	public boolean isEnabled() {
		return render;
	}

	public void queueUpdate() {
		needsUpdate = true;
	}

	public abstract void update();

	public void queueAndUpdate() {
		queueUpdate();
		update();
	}

	public Matrix4f getModelViewMatrix(Matrix4f viewMatrix) {
		return viewMatrix.mul(transformationMatrix, new Matrix4f());
	}

	public Matrix4f getModelMatrix() {
		return this.transformationMatrix;
	}

	public Matrix4f getTransformationMatrix() {
		transformationMatrix.identity()
				.translate(position)
				.rotate(rotation)
				.scale(scale);
		return transformationMatrix;
	}

	public Matrix4f getOrthoProjModelMatrix(Matrix4f orthoMatrix) {
		return orthoMatrix.mulLocal(getTransformationMatrix());
	}

	public Vector3f getPosition() {
		return position;
	}

	public Quaternionf getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}
}
