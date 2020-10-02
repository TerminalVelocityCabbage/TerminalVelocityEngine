package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Vector3f;

import java.util.ArrayList;

public class Model {

	ArrayList<Model.Part> modelParts;

	public Model(ArrayList<Model.Part> modelParts) {
		this.modelParts = modelParts;
	}

	public void setTranslation(Vector3f position, Vector3f rotation, Vector3f scale) {
		for (Model.Part part : modelParts) {
			part.setTranslation(position, rotation, scale);
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

	public void update() {
		for (Model.Part part : modelParts) {
			part.update();
		}
	}

	public static class Part {

		Mesh mesh;
		ArrayList<Model.Part> children;

		public Part(Mesh mesh, ArrayList<Model.Part> children) {
			this.mesh = mesh;
			this.children = children;
		}

		public ArrayList<Part> getChildren() {
			return children;
		}

		public void setTranslation(Vector3f position, Vector3f rotation, Vector3f scale) {
			mesh.setTranslation(position, rotation, scale);
			for (Model.Part child : children) {
				child.setTranslation(position, rotation, scale);
			}
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

		public void update() {
			mesh.update();
			for (Model.Part child : children) {
				child.update();
			}
		}
	}
}
