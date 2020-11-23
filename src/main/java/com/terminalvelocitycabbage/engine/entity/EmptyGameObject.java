package com.terminalvelocitycabbage.engine.entity;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class EmptyGameObject {

	Vector3f position;
	Quaternionf rotation;
	Vector3f scale;

	Matrix4f transformationMatrix;

	boolean needsUpdate = true;
	boolean render = false;

	public EmptyGameObject() {
		position = new Vector3f();
		rotation = new Quaternionf();
		scale = new Vector3f(1);
	}

	public EmptyGameObject(Vector3f position, Quaternionf rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void setPosition(float x, float y, float z) {
		this.position = this.position.set(x, y, z);
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
		queueUpdate();
	}

	public void rotate(float x, float y, float z) {
		rotation.add(-x, -y, -z, rotation.w, rotation);
		queueUpdate();
	}

	public void scale(float x, float y, float z) {
		scale.add(x, y, z);
		queueUpdate();
	}

	public void disable() {
		render = false;
	}

	public void enable() {
		render = true;
	}

	public void queueUpdate() {
		needsUpdate = true;
	}

	public abstract void update();

	public Matrix4f getModelViewMatrix(Matrix4f viewMatrix) {
		return viewMatrix.mul(transformationMatrix, new Matrix4f());
	}

	public Matrix4f getTransformationMatrix() {
		transformationMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);
		return transformationMatrix;
	}

	public Matrix4f getOrthoProjModelMatrix(Matrix4f orthoMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);
		Matrix4f orthoMatrixCurr = new Matrix4f(orthoMatrix);
		orthoMatrixCurr.mul(modelMatrix);
		return orthoMatrixCurr;
	}

	public static abstract class Builder {
		Vector3f position = null;
		Quaternionf rotation = null;
		Vector3f scale = null;

		public EmptyGameObject.Builder setPosition(Vector3f position) {
			this.position = position;
			return this;
		}

		public EmptyGameObject.Builder setRotation(Quaternionf rotation) {
			this.rotation = rotation;
			return this;
		}

		public EmptyGameObject.Builder setScale(Vector3f scale) {
			this.scale = scale;
			return this;
		}

		public abstract EmptyGameObject build();
	}
}
