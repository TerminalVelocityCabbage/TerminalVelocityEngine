package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderElement;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.debug.Log;
import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MeshPart {

    protected RenderFormat format;

    private int vertexOffset = -1;

    private final Vertex[] vertices;
    private final int[] vertexOrder;

    public MeshPart(Vertex[] vertices, int[] vertexOrder) {
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

    public MeshPart deepCopy() {
        MeshPart part = new MeshPart(
            Arrays.stream(this.vertices)
                .map(v -> new Vertex(v.getData()))
                .toArray(Vertex[]::new),
            IntStream.of(this.vertexOrder).toArray()
        );
        part.format = this.format;
        part.vertexOffset = this.vertexOffset;
        return part;
    }

    /**
     * Allocates this part in the mesh. Sets {@link #vertexOffset} to be the next vertex index.
     * @param indexBuffer The index buffer to add to.
     * @param counter The vertex counter.
     */
    public void allocate(IntBuffer indexBuffer, VertexCounter counter) {
        int vertexOffset = counter.getVertexIndex(this.verticesCount());
        this.vertexOffset = vertexOffset;
        for (int s : this.vertexOrder) {
            indexBuffer.put((vertexOffset + s));
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
                    float[] toModify = new float[element.getCount()];
                    System.arraycopy(data, offset, toModify, 0, element.getCount());
                    element.transform(toModify, translationMatrix);
                    System.arraycopy(toModify, 0, dataToSet, offset, element.getCount());
                } else {
                    System.arraycopy(data, offset, dataToSet, offset, element.getCount());
                }
                offset += element.getCount();
            }
            buffer.put(dataToSet);
        }
    }

}
