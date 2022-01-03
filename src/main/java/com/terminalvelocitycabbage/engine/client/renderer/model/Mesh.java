package com.terminalvelocitycabbage.engine.client.renderer.model;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderElement;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

	private final RenderFormat format;
	private final RenderMode mode;

	private int vaoID;
	private int vboID;
	private int eboID;

	private int indexCount;
	private int vertexCount;

	protected FloatBuffer vertexBuffer;
	protected IntBuffer indexBuffer;
	private Material material;

	public Mesh(RenderFormat format, RenderMode mode) {
		this.format = format;
		this.mode = mode;
	}

	//TODO note that these translations have to be done in a specific order so we should make it clear and make an API that dummi-proofs it
	//1. Scale	- so that the axis stuff is scaled properly
	//2. Offset	- so that rotations happen about the rotation point
	//3. Rotate	- so that the move action doesnt mess up the rotation point pos
	//4. Move	- because moving is dum

	public void createBuffers(int vertexCount, int indexCount) {
		this.vertexBuffer = BufferUtils.createFloatBuffer(vertexCount * this.format.getStride());
		this.indexBuffer = BufferUtils.createIntBuffer(indexCount);
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
		List<RenderElement> elementList = this.format.getElementList();
		int offset = 0;
		for (int i = 0; i < elementList.size(); i++) {
			RenderElement element = elementList.get(i);
			glVertexAttribPointer(i, element.getCount(), GL11.GL_FLOAT, false, this.format.getStrideBytes(), offset);
			offset += element.getSize();
		}

		//Enable the attrib pointers
		for (int i = 0; i < elementList.size(); i++) {
			glEnableVertexAttribArray(i);
		}

		//Create EBO for connected tris
		eboID = glGenBuffers();
	}

	public void render() {
		// Bind to the VAO that has all the information about the vertices
		glBindVertexArray(vaoID);

		//Bind Textures
		if (material != null && material.hasTexture()) {
			material.getTexture().bind();
		}

		// Bind to the index VBO/EBO that has all the information about the order of the vertices
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

		// Draw the vertices
		mode.applyWidth();
		glDrawElements(mode.getGlType(), indexCount, GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	public void destroy() {

		glBindVertexArray(vaoID);
		glBindBuffer(GL_ARRAY_BUFFER, vboID);

		for (int i = 0; i < format.getElementList().size(); i++) {
			glDisableVertexAttribArray(i);
		}

		glDeleteBuffers(vboID);
		glDeleteBuffers(eboID);
		glDeleteVertexArrays(vaoID);

		if (material != null && material.hasTexture()) {
			material.getTexture().destroy();
		}
	}

	public void updateVertexData() {
		vertexBuffer.position(vertexCount * this.format.getStride());
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

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public RenderFormat getFormat() {
		return format;
	}

	public void dumpAsObj() {
		try (PrintStream stream = new PrintStream(new FileOutputStream("./dump.obj"))) {
			int stride = this.format.getStride();
			float[] vertex = new float[this.vertexCount * stride];
			this.vertexBuffer.position(0);
			this.vertexBuffer.get(vertex);

			int[] index = new int[this.indexCount];
			this.indexBuffer.position(0);
			this.indexBuffer.get(index);

			int pos = this.format.getElementOffset(RenderElement.POSITION, 0);
			int uv = this.format.getElementOffset(RenderElement.UV, -1);
			int normal = this.format.getElementOffset(RenderElement.NORMAL, -1);

			String format = "%1$s";
			if(uv != -1) format += "/%1$s";
			if(normal != -1) format += "/%1$s";

			for (int i = 0; i < vertex.length; i += stride) {
				stream.println("v " + vertex[i+pos] + " " + vertex[i+pos+1] + " " + vertex[i+pos+2]);
				if(uv != -1) {
					stream.println("vt " + vertex[i+uv] + " " + vertex[i+uv+1]);
				}
				if(normal != -1) {
					stream.println("vn " + vertex[i+normal] + " " + vertex[i+normal+1] + " " + vertex[i+normal+2]);
				}
			}

			for (int i = 0; i < index.length; i+=3) {
				stream.println("f " + String.format(format, index[i]+1) + " " + String.format(format, index[i+1]+1) + " " + String.format(format, index[i+2]+1));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
