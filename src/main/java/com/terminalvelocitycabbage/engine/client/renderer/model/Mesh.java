package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedVertex;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedVertex.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public abstract class Mesh {

	Vector3f offset;
	Vector3f position;
	Vector3f rotation;
	Vector3f scale;

	private int vaoID;
	private int vboID;
	private int eboID;

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
		//TODO create worldUniform and render the object

		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(vaoID);
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		// Bind to the index VBO/EBO that has all the information about the order of the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

		// Draw the vertices
		glDrawElements(GL_TRIANGLES, indexOrder().length, GL_UNSIGNED_BYTE, 0);

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

	public void update() {
		// Put the new data in a ByteBuffer (in the view of a FloatBuffer)
		FloatBuffer vertexFloatBuffer = getCombinedVertices();
		for (int i = 0; i < numVertices(); i++) {
			vertexFloatBuffer.rewind();
			vertexFloatBuffer.put(getVertex(i).getElements());
			vertexFloatBuffer.flip();
			glBufferSubData(GL_ARRAY_BUFFER, i * STRIDE, vertexFloatBuffer);
		}
	}

	public TexturedVertex getVertex(int index) {
		return getVertices()[index];
	}

	public FloatBuffer getCombinedVertices() {
		FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(getVertices().length * TexturedVertex.ELEMENT_COUNT);
		for (TexturedVertex vertex : getVertices()) {
			verticesBuffer.put(vertex.getElements());
		}
		return verticesBuffer.flip();
	}

	private ByteBuffer getIndicesBuffer() {
		return BufferUtils.createByteBuffer(indexOrder().length).put(indexOrder()).flip();
	}

	public void setTranslation(Vector3f position, Vector3f rotation, Vector3f scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void offset(float x, float y, float z) {
		offset.add(x, y, z);
	}

	public void move(float x, float y, float z) {
		position.add(x, y, z);
	}

	public void rotate(float x, float y, float z) {
		rotation.add(x, y, z);
	}

	public void scaleAxis(float x, float y, float z) {
		scale.add(x, y, z);
	}

	public void scale(float amount) {
		scale.mul(amount);
	}

	public Vector3f getOffset() {
		return offset;
	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public abstract int numVertices();

	public abstract byte[] indexOrder();

	public abstract TexturedVertex[] getVertices();

}
