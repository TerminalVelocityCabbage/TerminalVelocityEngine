package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.VertexCounter;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TextRectangle;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextMeshPart extends MeshPart {

	private final TextVertex[] vertices;
	private final short[] vertexOrder;

	private int vertexOffset = -1;

	public TextMeshPart(TextRectangle rectangle) {
		this.vertices = rectangle.getVertices();
		this.vertexOrder = rectangle.getVertexOrder();
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
			throw new IllegalStateException("Vertex not allocated");
		}
		//Update the vertex attributes
		Vector4f positions = new Vector4f();
		Vector4f colors = new Vector4f();
		float[] currentXYZ;
		float[] currentColor;
		buffer.position(this.vertexOffset * TextVertex.ELEMENT_COUNT);
		for (TextVertex currentVertex : vertices) {
			currentXYZ = currentVertex.getXYZ();
			positions.set(currentXYZ[0], currentXYZ[1], currentXYZ[2], 1f).mul(translationMatrix);
			currentColor = currentVertex.getColor();
			colors.set(currentColor[0], currentColor[1], currentColor[2], 1f);

			// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
			buffer.put(TextVertex.getElements(positions.x, positions.y, positions.z, currentVertex.getUV(), colors.x, colors.y, colors.z, colors.w));
		}
	}
}
