package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public abstract class MeshPart {

    private int vertexOffset = -1;

    public abstract int verticesCount();

    public abstract int vertexOrderCount();

    /**
     * Allocates this part in the mesh. Sets {@link #vertexOffset} to be the next vertex index.
     * @param indexBuffer The index buffer to add to.
     * @param counter The vertex counter.
     */
    public abstract void allocate(ShortBuffer indexBuffer, VertexCounter counter);

    /**
     * Updates the floatbuffer with this objects vertices. Uses {@link #vertexOffset}
     * @param translationMatrix The current translation matrix
     * @param buffer the buffer to update into.
     */
    public abstract void update(Matrix4f translationMatrix, FloatBuffer buffer);

}
