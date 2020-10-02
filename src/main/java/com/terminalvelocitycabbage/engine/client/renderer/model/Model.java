package com.terminalvelocitycabbage.engine.client.renderer.model;

import java.util.ArrayList;

public class Model {

	ArrayList<Model.Part> modelParts;

	public Model(ArrayList<Model.Part> modelParts) {
		this.modelParts = modelParts;
	}

	public void move(float x, float y, float z) {
		for (Model.Part part : modelParts) {
			part.move(x, y, z);
		}
		update();
	}

	public void rotate(float x, float y, float z) {
		for (Model.Part part : modelParts) {
			part.rotate(x, y, z);
		}
		update();
	}

	public void scale(float x, float y, float z) {
		for (Model.Part part : modelParts) {
			part.scale(x, y, z);
		}
		update();
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

		public void move(float x, float y, float z) {
			mesh.move(x, y, z);
			for (Model.Part child : children) {
				child.move(x, y, z);
			}
			update();
		}

		public void rotate(float x, float y, float z) {
			mesh.rotate(x, y, z);
			for (Model.Part child : children) {
				child.rotate(x, y, z);
			}
			update();
		}

		public void scale(float x, float y, float z) {
			mesh.scaleAxis(x, y, z);
			for (Model.Part child : children) {
				child.scale(x, y, z);
			}
			update();
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
