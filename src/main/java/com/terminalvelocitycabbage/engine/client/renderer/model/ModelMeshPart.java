package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ModelMeshPart extends MeshPart {

	private final ModelVertex[] vertices;
	private final short[] vertexOrder;

	private int vertexOffset = -1;

	public ModelMeshPart(ModelVertex[] vertices, short[] vertexOrder) {
		this.vertices = vertices;
		this.vertexOrder = vertexOrder;
	}

	@Override
	public int verticesCount() {
		return vertices.length;
	}

	@Override
	public int vertexOrderCount() {
		return vertexOrder.length;
	}

	@Override
	public void allocate(ShortBuffer indexBuffer, VertexCounter counter) {
		short vertexOffset = (short) counter.getVertexIndex(this.verticesCount());
		this.vertexOffset = vertexOffset;
		for (short s : this.vertexOrder) {
			indexBuffer.put((short) (vertexOffset + s));
		}
	}

	@Override
	public void update(Matrix4f translationMatrix, FloatBuffer buffer) {
		if (this.vertexOffset == -1) {
			Log.crash("Mesh Bind Error", new IllegalStateException("Vertex not allocated"));
		}
		//Update the vertex positions
		Vector4f positions = new Vector4f();
		Vector4f normals = new Vector4f();
		float[] currentXYZ;
		float[] currentNormal;
		buffer.position(this.vertexOffset * ModelVertex.ELEMENT_COUNT);
		for (ModelVertex currentVertex : vertices) {
			currentXYZ = currentVertex.getXYZ();
			positions.set(currentXYZ[0], currentXYZ[1], currentXYZ[2], 1f).mul(translationMatrix);
			currentNormal = currentVertex.getNormals();
			normals.set(currentNormal[0], currentNormal[1], currentNormal[2], 1f).rotate(translationMatrix.getUnnormalizedRotation(new Quaternionf()));

			// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
			buffer.put(ModelVertex.getElements(positions.x, positions.y, positions.z, currentVertex.getUV(), normals.x, normals.y, normals.z));
		}
	}
}
