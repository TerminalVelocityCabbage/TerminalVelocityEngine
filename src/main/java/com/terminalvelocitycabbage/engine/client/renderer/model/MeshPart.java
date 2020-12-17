package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class MeshPart {

    private final ModelVertex[] vertices;
    private final short[] vertexOrder;

    private int vertexOffset = -1;

    public MeshPart(ModelVertex[] vertices, short[] vertexOrder) {
        this.vertices = vertices;
        this.vertexOrder = vertexOrder;
    }

    public int verticesCount() {
        return this.vertices.length;
    }

    public int vertexOrderCount() {
        return this.vertexOrder.length;
    }

    /**
     * Allocates this part in the mesh. Sets {@link #vertexOffset} to be the next vertex index.
     * @param indexBuffer The index buffer to add to.
     * @param counter The vertex counter.
     */
    public void allocate(ShortBuffer indexBuffer, Model.VertexCounter counter) {
        short vertexOffset = (short) counter.getVertexIndex(this.verticesCount());
        this.vertexOffset = vertexOffset;
        for (short s : this.vertexOrder) {
            indexBuffer.put((short) (vertexOffset + s));
        }
    }

    /**
     * Updates the floatbuffer with this objects vertices. Uses {@link #vertexOffset}
     * @param translationMatrix The current translation matrix
     * @param buffer the buffer to update into.
     */
    public void update(Matrix4f translationMatrix, FloatBuffer buffer) {
        if(this.vertexOffset == -1) {
            throw new IllegalStateException("Vertex not allocated");
        }
        //Update the vertex positions
        Vector4f positions = new Vector4f();
        Vector4f normals = new Vector4f();
        ModelVertex currentVertex;
        float[] currentXYZ;
        float[] currentNormal;
        for (int i = 0; i < vertices.length; i++) {
            currentVertex = vertices[i];
            currentXYZ = currentVertex.getXYZ();
            positions.set(currentXYZ[0], currentXYZ[1], currentXYZ[2], 1f).mul(translationMatrix);
            currentNormal = currentVertex.getNormals();
            normals.set(currentNormal[0], currentNormal[1], currentNormal[2], 1f).rotate(translationMatrix.getUnnormalizedRotation(new Quaternionf()));

            // Put the new data in a ByteBuffer (in the view of a FloatBuffer)
            buffer.put((this.vertexOffset + i) * ModelVertex.ELEMENT_COUNT, ModelVertex.getElements(positions.x, positions.y, positions.z, currentVertex.getUV(), normals.x, normals.y, normals.z));
        }
    }

}
