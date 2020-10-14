package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static com.terminalvelocitycabbage.engine.client.renderer.model.Vertex.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh {

	private int vaoID;
	private int vboID;
	private int eboID;

	protected Vertex[] vertices;
	protected byte[] vertexOrder;

	//TODO note that these translations have to be done in a specific order so we should make it clear and make an API that dummi-proofs it
	//1. Scale	- so that the axis stuff is scaled properly
	//2. Offset	- so that rotations happen about the rotation point
	//3. Rotate	- so that the move action doesnt mess up the rotation point pos
	//4. Move	- because moving is dum

	public void bind() {
		//Create the VAO and bind it
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Create the VBO and bind it
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, getCombinedVertices(), GL_STATIC_DRAW);

		//Define vertex data for shader
		glVertexAttribPointer(0, POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, POSITION_OFFSET);
		glVertexAttribPointer(1, COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, COLOR_OFFSET);
		glVertexAttribPointer(2, TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, TEXTURE_OFFSET);

		//Create EBO for connected tris
		eboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, getIndicesBuffer(), GL_STATIC_DRAW);
	}

	public void render() {
		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		// Bind to the index VBO/EBO that has all the information about the order of the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

		// Draw the vertices
		glDrawElements(GL_TRIANGLES, vertexOrder.length, GL_UNSIGNED_BYTE, 0);

		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
	}

	public void destroy() {
		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
		glDeleteVertexArrays(vaoID);
	}

	public void update(Matrix4f translationMatrix) {
		//Update the vertex positions
		Vector4f positions = new Vector4f();
		Vertex currentVertex;
		FloatBuffer vertexFloatBuffer = getCombinedVertices();
		for (int i = 0; i < vertices.length; i++) {
			currentVertex = getVertex(i);

			float[] xyz = currentVertex.getXYZ();
			positions.set(xyz[0], xyz[1], xyz[2], 1F).mul(translationMatrix);

			// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
			vertexFloatBuffer.rewind();
			var vertex = getVertex(i);
			vertexFloatBuffer.put(Vertex.getElements( new float[] { positions.x, positions.y, positions.z, positions.w },  vertex.getRGBA(), vertex.getUV() ));
			vertexFloatBuffer.flip();

			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			glBufferSubData(GL_ARRAY_BUFFER, i * STRIDE, vertexFloatBuffer);
		}
	}

	public Vertex getVertex(int index) {
		return vertices[index];
	}

	public FloatBuffer getCombinedVertices() {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.ELEMENT_COUNT);
		for (Vertex vertex : vertices) {
			verticesBuffer.put(Vertex.getElements(vertex.getXYZ(), vertex.getRGBA(), vertex.getUV()));
		}
		return verticesBuffer.flip();
	}

	private ByteBuffer getIndicesBuffer() {
		return BufferUtils.createByteBuffer(vertexOrder.length).put(vertexOrder).flip();
	}
}
