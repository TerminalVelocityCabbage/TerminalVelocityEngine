package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.List;

/**
 * The Model class is only a container for a list of model parts
 * This class also handles passing calls from the game object down to the model parts who handle meshes
 * Models have no point in space however model parts do contain translations
 */
public class Model {

	public List<Part> modelParts;
	public Material material;
	//To avoid creating a new one every part render call
	Matrix4f transformationMatrix;

	public Model(List<Model.Part> modelParts) {
		this.modelParts = modelParts;
		for (Part part : modelParts) {
			part.setModel(this);
		}
		this.transformationMatrix = new Matrix4f();
	}

	public void update(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.identity().translate(position).
			rotateX((float)Math.toRadians(-rotation.x)).
			rotateY((float)Math.toRadians(-rotation.y)).
			rotateZ((float)Math.toRadians(-rotation.z)).
			scale(scale);
		var mat = new Matrix4f(transformationMatrix);
		for (Model.Part part : modelParts) {
			part.updateTransforms(mat);
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

		private Model model;

		public ModelMesh mesh;
		public List<Model.Part> children;

		public Vector3f offset;
		public Vector3f position;
		public Vector3f rotation;
		public Vector3f scale;

		public Part(ModelMesh mesh) {
			this.mesh = mesh;

			this.offset = new Vector3f();
			this.position = new Vector3f();
			this.rotation = new Vector3f();
			this.scale = new Vector3f(1);

			this.children = Collections.emptyList();
		}

		public Part(ModelMesh mesh, Vector3f offset, Vector3f position, Vector3f rotation, Vector3f scale, List<Model.Part> children) {
			this.mesh = mesh;

			this.offset = offset;
			this.position = position;
			this.rotation = rotation;
			this.scale = scale;

			this.children = children;
		}

		public void bind() {
			if (mesh != null) mesh.bind();
			for (Model.Part child : children) {
				child.bind();
			}
		}

		public void render() {
			if (mesh != null) mesh.render();
			for (Model.Part child : children) {
				child.render();
			}
		}

		public void destroy() {
			if (mesh != null) mesh.destroy();
			for (Model.Part child : children) {
				child.destroy();
			}
		}

		public void updateTransforms(Matrix4f transformationMatrix) {
			transformationMatrix
				.translate(position)
				.rotateX((float) Math.toRadians(rotation.x))
				.rotateY((float) Math.toRadians(rotation.y))
				.rotateZ((float) Math.toRadians(rotation.z));
			var mat = new Matrix4f();
			for (Model.Part child : children) {
				child.updateTransforms(mat.set(transformationMatrix));
			}
			transformationMatrix
				.translate(offset)
				.scale(scale);
			mesh.update(transformationMatrix);
		}

		public void setModel(Model model) {
			this.model = model;
			this.mesh.model = model;
			for (Part part : children) {
				part.setModel(this.model);
			}
		}
	}

	public Model setMaterial(Material material) {
		this.material = material;
		return this;
	}

	public Material getMaterial() {
		return material;
	}
}
