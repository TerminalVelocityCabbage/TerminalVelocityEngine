package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Collections;
import java.util.List;

public class TextModel {

	public List<TextModel.Character> characters;
	//TODO reconsider using a mesh here at all, might not need it
	public TextMesh mesh;
	Matrix4f transformationMatrix;

	public TextModel(List<TextModel.Character> characters) {
		this.characters = characters;
		this.mesh = new TextMesh();
		this.transformationMatrix = new Matrix4f();
	}

	/**
	 * To be called whenever `characters` changes.
	 */
	public void resizeBuffer() {
		int vertexCount = 0;
		for (TextModel.Character character : this.characters) {
			vertexCount += character.getTotalVertexCount();
		}

		int indexCount = 0;
		for (TextModel.Character character : this.characters) {
			indexCount += character.getTotalIndexCount();
		}

		this.mesh.createBuffers(vertexCount, indexCount);

		VertexCounter counter = new VertexCounter();
		for (TextModel.Character character : this.characters) {
			character.allocateMesh(this.mesh.indexBuffer, counter);
		}

		this.mesh.updateIndexData();
	}

	public void update(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);

		this.mesh.vertexBuffer.rewind();

		for (TextModel.Character character : characters) {
			character.updateMeshes(new Matrix4f(transformationMatrix), this.mesh.vertexBuffer);
		}

		this.mesh.updateVertexData();
	}

	public void bind() {
		this.mesh.bind();
		this.resizeBuffer();
	}

	public void render() {
		this.mesh.render();
	}

	public void destroy() {
		this.mesh.destroy();
	}

	public static class Character {
		public MeshPart meshPart;
		public List<Model.Part> children;

		public Vector3f offset;
		public Vector3f position;
		public Vector3f rotation;
		public Vector3f scale;

		public Character(MeshPart part) {
			this.meshPart = part;

			this.offset = new Vector3f();
			this.position = new Vector3f();
			this.rotation = new Vector3f();
			this.scale = new Vector3f(1);

			this.children = Collections.emptyList();
		}

		public Character(MeshPart meshPart, Vector3f offset, Vector3f position, Vector3f rotation, Vector3f scale, List<Model.Part> children) {
			this.meshPart = meshPart;

			this.offset = offset;
			this.position = position;
			this.rotation = rotation;
			this.scale = scale;

			this.children = children;
		}

		public void updateMeshes(Matrix4f transformationMatrix, FloatBuffer buffer) {
			transformationMatrix
					.translate(position)
					.rotateX(rotation.x)
					.rotateY(rotation.y)
					.rotateZ(rotation.z);
			var mat = new Matrix4f();
			for (Model.Part child : children) {
				child.updateMeshes(mat.set(transformationMatrix), buffer);
			}
			transformationMatrix
					.translate(offset)
					.scale(scale);
			this.meshPart.update(transformationMatrix, buffer);
		}

		public int getTotalVertexCount() {
			int vertexCount = this.meshPart.verticesCount();
			for (Model.Part child : this.children) {
				vertexCount += child.getTotalVertexCount();
			}
			return vertexCount;
		}

		public int getTotalIndexCount() {
			int vertexCount = this.meshPart.vertexOrderCount();
			for (Model.Part child : this.children) {
				vertexCount += child.getTotalIndexCount();
			}
			return vertexCount;
		}

		public void allocateMesh(ShortBuffer indexBuffer, VertexCounter counter) {
			for (Model.Part child : this.children) {
				child.allocateMesh(indexBuffer, counter);
			}
			this.meshPart.allocate(indexBuffer, counter);
		}
	}

	public TextModel setFontTexture(FontTexture texture) {
		this.mesh.setFontTexture(texture);
		return this;
	}

	public FontTexture getMaterial() {
		return this.mesh.getFontTexture();
	}
}
