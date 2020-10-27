package com.terminalvelocitycabbage.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public abstract class EmptyGameObject {

	Vector3f position;
	Vector3f rotation;
	Vector3f scale;

	Matrix4f modelViewMatrix;

	boolean needsUpdate = true;

	public EmptyGameObject(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
		needsUpdate = true;
	}

	public void rotate(float x, float y, float z) {
		rotation.sub(x, y, z);
		needsUpdate = true;
	}

	public void scale(float x, float y, float z) {
		scale.add(x, y, z);
		needsUpdate = true;
	}

	public void queueUpdate() {
		needsUpdate = true;
	}

	public abstract void bind();

	public abstract void render();

	public abstract void destroy();

	public void update() {
		if(needsUpdate) {
			needsUpdate = false;
		}
	}

	public Matrix4f getModelViewMatrix(Matrix4f viewMatrix) {
		modelViewMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(modelViewMatrix);
	}

	public static abstract class Builder {
		Vector3f position = null;
		Vector3f rotation = null;
		Vector3f scale = null;

		public EmptyGameObject.Builder setPosition(Vector3f position) {
			this.position = position;
			return this;
		}

		public EmptyGameObject.Builder setRotation(Vector3f rotation) {
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
