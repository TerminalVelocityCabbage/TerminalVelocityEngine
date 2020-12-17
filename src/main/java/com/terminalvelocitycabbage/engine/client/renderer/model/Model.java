package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.util.Collections;
import java.util.List;

/**
 * The Model class is only a container for a list of model parts
 * This class also handles passing calls from the game object down to the model parts who handle meshes
 * Models have no point in space however model parts do contain translations
 */
public class Model {

	public List<Part> modelParts;
	private Material material;
	public ModelMesh mesh = new ModelMesh();
	//To avoid creating a new one every part render call
	Matrix4f transformationMatrix;

	public Model(List<Model.Part> modelParts) {
		this.modelParts = modelParts;
		this.transformationMatrix = new Matrix4f();
	}

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
			part.allocateMesh(this.mesh, counter);
		}

		this.mesh.updateVertexIndexData();

	}

	public void update(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.identity().translate(position).
			rotateX((float)Math.toRadians(-rotation.x)).
			rotateY((float)Math.toRadians(-rotation.y)).
			rotateZ((float)Math.toRadians(-rotation.z)).
			scale(scale);

		this.mesh.vertexBuffer.rewind();
		this.mesh.vertexBuffer.limit(this.mesh.vertexBuffer.capacity());

		for (Model.Part part : modelParts) {
			part.updateMeshes(new Matrix4f(transformationMatrix), this.mesh.vertexBuffer);
		}

//		float[] arr = new float[this.mesh.vertexBuffer.capacity()];
//		this.mesh.vertexBuffer.rewind();
//		this.mesh.vertexBuffer.get(arr);
//		System.out.println(Arrays.toString(arr));
//		this.mesh.vertexBuffer.rewind();

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

	public static class Part {
		public MeshPart meshPart;
		public List<Model.Part> children;

		public Vector3f offset;
		public Vector3f position;
		public Vector3f rotation;
		public Vector3f scale;

		public Part(MeshPart part) {
			this.meshPart = part;

			this.offset = new Vector3f();
			this.position = new Vector3f();
			this.rotation = new Vector3f();
			this.scale = new Vector3f(1);

			this.children = Collections.emptyList();
		}

		public Part(MeshPart meshPart, Vector3f offset, Vector3f position, Vector3f rotation, Vector3f scale, List<Model.Part> children) {
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

		public void allocateMesh(ModelMesh mesh, VertexCounter counter) {
			for (Part child : this.children) {
				child.allocateMesh(mesh, counter);
			}
			this.meshPart.allocate(mesh, counter);
		}
	}

	public void setMaterial(Material material) {
		this.material = material;
		this.mesh.setMaterial(material);
	}

	public Material getMaterial() {
		return material;
	}

	protected static class VertexCounter {
		private int vertexCount;

		public int getVertexIndex(int count) {
			int ret = this.vertexCount;
			this.vertexCount += count;
			return ret;
		}
	}
}
