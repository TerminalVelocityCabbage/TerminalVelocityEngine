package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;

/**
 * The Model class is only a container for a list of model parts
 * This class also handles passing calls from the game object down to the model parts who handle meshes
 * Models have no point in space however model parts do contain translations
 */
public class Model {

	public List<Part> modelParts;
	public Mesh mesh;
	//To avoid creating a new one every part render call
	Matrix4f transformationMatrix;

	public Model(RenderFormat format, RenderMode mode, List<Model.Part> modelParts) {
		this.modelParts = modelParts;
		this.mesh = new Mesh(format, mode);
		this.transformationMatrix = new Matrix4f();
		this.onPartsChange();
	}

	public void onPartsChange() {
		for (Part part : this.modelParts) {
			part.setFormat(this.mesh.getFormat());
		}
	}

	/**
	 * To be called whenever `modelParts` changes.
	 */
	public void resizeBuffer() {
		int vertexCount = 0;
		for (Part part : this.modelParts) {
			vertexCount += part.getTotalVertexCount();
		}

		int indexCount = 0;
		for (Part modelPart : this.modelParts) {
			indexCount += modelPart.getTotalIndexCount();
		}

		this.mesh.createBuffers(vertexCount, indexCount);

		VertexCounter counter = new VertexCounter();
		for (Part part : this.modelParts) {
			part.allocateMesh(this.mesh.indexBuffer, counter);
		}

		this.mesh.updateIndexData();
	}

	public void update(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.translation(position)
			.rotate(rotation)
			.scale(scale);

		this.mesh.vertexBuffer.rewind();

		for (Model.Part part : modelParts) {
			part.updateMeshes(new Matrix4f(transformationMatrix), this.mesh.vertexBuffer);
		}

		this.mesh.updateVertexData();
//		this.mesh.dumpAsObj();
	}

	public void bind() {
		this.mesh.bind();
		resizeBuffer();
	}

	public void render() {
		this.mesh.render();
	}

	public void destroy() {
		this.mesh.destroy();
	}

	public static class Part {
		public MeshPart meshPart;
		public List<Model.Part> children;

		public Vector3f offset;
		public Vector3f position;
		public Quaternionf rotation;
		public Vector3f scale;

		public Part(MeshPart part) {
			this.meshPart = part;

			this.offset = new Vector3f();
			this.position = new Vector3f();
			this.rotation = new Quaternionf();
			this.scale = new Vector3f(1);

			this.children = Collections.emptyList();
		}

		public Part(MeshPart meshPart, Vector3f offset, Vector3f position, Quaternionf rotation, Vector3f scale, List<Model.Part> children) {
			this.meshPart = meshPart;

			this.offset = offset;
			this.position = position;
			this.rotation = rotation;
			this.scale = scale;

			this.children = children;
		}

		public void updateMeshes(Matrix4f transformationMatrix, FloatBuffer buffer) {
			transformationMatrix
				.translate(position).rotate(rotation);
			var mat = new Matrix4f();
			for (Model.Part child : children) {
				child.updateMeshes(mat.set(transformationMatrix), buffer);
			}
			transformationMatrix
				.translate(offset)
				.scale(scale);
			this.meshPart.update(transformationMatrix, buffer);
		}

		public void setFormat(RenderFormat format) {
			this.meshPart.setFormat(format);
			for (Part child : this.children) {
				child.setFormat(format);
			}
		}

		public int getTotalVertexCount() {
			int vertexCount = this.meshPart.verticesCount();
			for (Part child : this.children) {
				vertexCount += child.getTotalVertexCount();
			}
			return vertexCount;
		}

		public int getTotalIndexCount() {
			int vertexCount = this.meshPart.vertexOrderCount();
			for (Part child : this.children) {
				vertexCount += child.getTotalIndexCount();
			}
			return vertexCount;
		}

		public void allocateMesh(IntBuffer indexBuffer, VertexCounter counter) {
			for (Part child : this.children) {
				child.allocateMesh(indexBuffer, counter);
			}
			this.meshPart.allocate(indexBuffer, counter);
		}
	}

	public Model setMaterial(Material material) {
		this.mesh.setMaterial(material);
		return this;
	}

	public Material getMaterial() {
		return this.mesh.getMaterial();
	}
}
