package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * The Model class is only a container for a list of model parts
 * This class also handles passing calls from the game object down to the model parts who handle meshes
 * Models have no point in space however model parts do contain translations
 */
public abstract class Model {

	ArrayList<Model.Part> modelParts;
	//To avoid creating a new one every part render call
	Matrix4f transformationMatrix;

	public Model(ArrayList<Model.Part> modelParts) {
		this.modelParts = modelParts;
		this.transformationMatrix = new Matrix4f();
	}

	public void update(Vector3f position, Vector3f rotation, Vector3f scale) {
		for (Model.Part part : modelParts) {
			part.updateTransforms(transformationMatrix.identity().translate(position).
					rotateX((float)Math.toRadians(-rotation.x)).
					rotateY((float)Math.toRadians(-rotation.y)).
					rotateZ((float)Math.toRadians(-rotation.z)).
					scale(scale));
		}
	}

	public void bind() {
		for (Model.Part part : modelParts) {
			part.bind();
		}
	}

	public void render() {
		for (Model.Part part : modelParts) {
			part.render();
		}
	}

	public void destroy() {
		for (Model.Part part : modelParts) {
			part.destroy();
		}
	}

	public static class Part {

		private Mesh mesh;
		private ArrayList<Model.Part> children;

		Vector3f offset;
		Vector3f position;
		Vector3f rotation;
		Vector3f scale;

		Matrix4f transformationMatrix;

		public Part(Mesh mesh, Vector3f offset, Vector3f position, Vector3f rotation, Vector3f scale, ArrayList<Model.Part> children) {
			this.mesh = mesh;

			this.offset = offset;
			this.position = position;
			this.rotation = rotation;
			this.scale = scale;

			this.children = children;
			this.transformationMatrix = new Matrix4f();
		}

		public void bind() {
			mesh.bind();
			for (Model.Part child : children) {
				child.bind();
			}
		}

		public void render() {
			mesh.render();
			for (Model.Part child : children) {
				child.render();
			}
		}

		public void destroy() {
			mesh.destroy();
			for (Model.Part child : children) {
				child.destroy();
			}
		}

		public void updateTransforms(Matrix4f transformationMatrix) {
			this.transformationMatrix = transformationMatrix;
		}

		public void update() {
			transformationMatrix
					.translate(position)
					.rotateX(rotation.x)
					.rotateY(rotation.y)
					.rotateZ(rotation.z)
					.translate(offset)
					.scale(scale);
			mesh.update(transformationMatrix);
			for (Model.Part child : children) {
				child.update();
			}
		}
	}
}
