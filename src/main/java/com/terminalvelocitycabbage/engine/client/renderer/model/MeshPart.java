package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderElement;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

public class MeshPart {

    protected RenderFormat format;

    private int vertexOffset = -1;

    private final Vertex[] vertices;
    private final short[] vertexOrder;

    public MeshPart(Vertex[] vertices, short[] vertexOrder) {
        this.vertices = vertices;
        this.vertexOrder = vertexOrder;
    }

    public void setFormat(RenderFormat format) {
        this.format = format;
        for (Vertex vertex : this.vertices) {
            vertex.setFormat(format);
        }
    }

    public int verticesCount() {
        return vertices.length;
    }

    public int vertexOrderCount() {
        return vertexOrder.length;
    }

    public Vertex getVertex(int i) {
        return this.vertices[i];
    }

    /**
     * Allocates this part in the mesh. Sets {@link #vertexOffset} to be the next vertex index.
     * @param indexBuffer The index buffer to add to.
     * @param counter The vertex counter.
     */
    public void allocate(ShortBuffer indexBuffer, VertexCounter counter) {
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
        if (this.vertexOffset == -1) {
            Log.crash("Mesh Bind Error", new IllegalStateException("Vertex not allocated"));
        }
        List<RenderElement> elementList = this.format.getElementList();
        buffer.position(this.vertexOffset * this.format.getStride());
        for (Vertex currentVertex : vertices) {
            float[] data = currentVertex.getData();
            float[] dataToSet = new float[data.length];
            int offset = 0;
            for (RenderElement element : elementList) {
                if (element.hasTransformer()) {
                    Vector4f set = new Vector4f().set(currentVertex.getX(), currentVertex.getY(), currentVertex.getZ(), 1F);
                    set.mul(translationMatrix);
                    dataToSet[offset] = set.x;
                    dataToSet[offset+1] = set.y;
                    dataToSet[offset+2] = set.z;
                } else {
                    System.arraycopy(data, offset, dataToSet, offset, element.getCount());
                }
                offset += element.getCount();
            }
            buffer.put(dataToSet);
        }
    }

}
