package com.terminalvelocitycabbage.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

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
		GL20.glVertexAttribPointer(0, Vertex.POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.POSITION_OFFSET);
		GL20.glVertexAttribPointer(1, Vertex.COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.COLOR_OFFSET);
		GL20.glVertexAttribPointer(2, Vertex.TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, Vertex.STRIDE, Vertex.TEXTURE_OFFSET);

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
		float[] currentXYZ;
		for (int i = 0; i < vertices.length; i++) {
			currentVertex = getVertex(i);
			currentXYZ = currentVertex.getXYZ();
			positions.set(currentXYZ[0], currentXYZ[1], currentXYZ[2], 1f).mul(translationMatrix);

			// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
			vertexFloatBuffer.rewind();
			vertexFloatBuffer.put(Vertex.getElements( new float[] { positions.x, positions.y, positions.z, positions.w },  currentVertex.getRGBA(), currentVertex.getUV() ));
			vertexFloatBuffer.flip();

			//Pass new data to OpenGL
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			GL15.glBufferSubData(GL_ARRAY_BUFFER, i * Vertex.STRIDE, vertexFloatBuffer);
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
