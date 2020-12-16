package com.terminalvelocitycabbage.engine.client.renderer.model;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.terminalvelocitycabbage.engine.client.renderer.model.ModelVertex.*;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class ModelMesh {

	private int vaoID;
	private int vboID;
	private int eboID;

	protected ModelVertex[] vertices;
	protected byte[] vertexOrder;

	protected Model model;

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
		glVertexAttribPointer(1, TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, TEXTURE_OFFSET);
		glVertexAttribPointer(2, NORMAL_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, NORMAL_OFFSET);

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

		//Bind Textures
		if (model.getMaterial().hasTexture()) {
			model.getMaterial().getTexture().bind();
		}

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
		if (model.getMaterial().hasTexture()) {
			model.getMaterial().getTexture().destroy();
		}
		/*
		if (model.getMaterial().hasReflectivityTexture()) {
			model.getMaterial().getReflectivityTexture().destroy();
		}
		 */
	}

	public void update(Matrix4f translationMatrix) {
		//Update the vertex positions
		Vector4f positions = new Vector4f();
		Vector4f normals = new Vector4f();
		ModelVertex currentVertex;
		FloatBuffer vertexFloatBuffer = getCombinedVertices();
		float[] currentXYZ;
		float[] currentNormal;
		for (int i = 0; i < vertices.length; i++) {
			currentVertex = getVertex(i);
			currentXYZ = currentVertex.getXYZ();
			positions.set(currentXYZ[0], currentXYZ[1], currentXYZ[2], 1f).mul(translationMatrix);
			currentNormal = currentVertex.getNormals();
			normals.set(currentNormal[0], currentNormal[1], currentNormal[2], 1f).rotate(translationMatrix.getNormalizedRotation(new Quaternionf()));

			// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
			vertexFloatBuffer.rewind();
			vertexFloatBuffer.put(ModelVertex.getElements( new float[] { positions.x, positions.y, positions.z }, currentVertex.getUV(), new float[] { normals.x, normals.y, normals.z } ));
			vertexFloatBuffer.flip();

			//Pass new data to OpenGL
			glBindBuffer(GL_ARRAY_BUFFER, vboID);
			GL15.glBufferSubData(GL_ARRAY_BUFFER, i * ModelVertex.STRIDE, vertexFloatBuffer);
		}
	}

	public ModelVertex getVertex(int index) {
		return vertices[index];
	}

	public FloatBuffer getCombinedVertices() {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * ModelVertex.ELEMENT_COUNT);
		for (ModelVertex vertex : vertices) {
			verticesBuffer.put(ModelVertex.getElements(vertex.getXYZ(), vertex.getUV(), vertex.getNormals()));
		}
		return verticesBuffer.flip();
	}

	private ByteBuffer getIndicesBuffer() {
		return BufferUtils.createByteBuffer(vertexOrder.length).put(vertexOrder).flip();
	}
}
