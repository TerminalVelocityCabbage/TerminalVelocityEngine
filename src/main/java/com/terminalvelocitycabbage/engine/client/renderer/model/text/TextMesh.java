package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static com.terminalvelocitycabbage.engine.client.renderer.model.text.TextVertex.*;
import static com.terminalvelocitycabbage.engine.client.renderer.model.Vertex.POSITION_ELEMENT_COUNT;
import static com.terminalvelocitycabbage.engine.client.renderer.model.Vertex.POSITION_OFFSET;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

public class TextMesh {

	private int vaoID;
	private int vboID;
	private int eboID;

	private int indexCount;
	private int vertexCount;

	protected FloatBuffer vertexBuffer;
	protected ShortBuffer indexBuffer;

	protected FontTexture fontTexture;

	public TextMesh(FontTexture fontTexture) {
		this.fontTexture = fontTexture;
	}

	//TODO note that these translations have to be done in a specific order so we should make it clear and make an API that dummi-proofs it
	//1. Scale	- so that the axis stuff is scaled properly
	//2. Offset	- so that rotations happen about the rotation point
	//3. Rotate	- so that the move action doesnt mess up the rotation point pos
	//4. Move	- because moving is dum

	public void createBuffers(int vertexCount, int indexCount) {
		this.vertexBuffer = BufferUtils.createFloatBuffer(vertexCount * TextVertex.ELEMENT_COUNT);
		this.indexBuffer = BufferUtils.createShortBuffer(indexCount);
		this.indexCount = indexCount;
		this.vertexCount = vertexCount;
	}

	public void bind() {
		//Create the VAO and bind it
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);

		//Create the VBO and bind it
		vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);

		//Define vertex data for shader
		glVertexAttribPointer(0, POSITION_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, POSITION_OFFSET);
		glVertexAttribPointer(1, TEXTURE_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, TEXTURE_OFFSET);
		glVertexAttribPointer(2, COLOR_ELEMENT_COUNT, GL11.GL_FLOAT, false, STRIDE, COLOR_OFFSET);

		//Enable the attrib pointers
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);

		//Create EBO for connected tris
		eboID = glGenBuffers();
	}

	public void render() {
		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(vaoID);

		//Bind Textures
		fontTexture.bind();

		// Bind to the index VBO/EBO that has all the information about the order of the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

		// Draw the vertices
		glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_SHORT, 0);

		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void destroy() {

		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);

		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
		glDeleteVertexArrays(vaoID);

		fontTexture.destroy();
	}

	public void updateVertexData() {
		vertexBuffer.position(vertexCount * TextVertex.ELEMENT_COUNT);
		vertexBuffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
	}

	public void updateIndexData() {
		indexBuffer.position(indexCount);
		indexBuffer.flip();

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
	}

	public void setFontTexture(FontTexture fontTexture) {
		this.fontTexture = fontTexture;
	}

	public FontTexture getFontTexture() {
		return fontTexture;
	}
}
