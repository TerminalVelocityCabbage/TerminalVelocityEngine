package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import com.terminalvelocitycabbage.engine.client.renderer.model.VertexCounter;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontTexture;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TextModel {

	public List<TextCharacter> textCharacters = new ArrayList<>();
	public TextMesh mesh;
	Matrix4f transformationMatrix;
	public int width = 0;

	public TextModel(FontTexture fontTexture) {
		this.mesh = new TextMesh(fontTexture);
		this.transformationMatrix = new Matrix4f();
	}

	/**
	 * To be called whenever `characters` changes.
	 */
	public void resizeBuffer() {
		int vertexCount = 0;
		int indexCount = 0;
		for (TextCharacter character : textCharacters) {
			vertexCount += character.getTotalVertexCount();
			indexCount += character.getTotalIndexCount();
		}

		this.mesh.createBuffers(vertexCount, indexCount);

		VertexCounter counter = new VertexCounter();
		for (TextCharacter character : textCharacters) {
			character.allocateMesh(this.mesh.indexBuffer, counter);
		}

		this.mesh.updateIndexData();
	}

	public void update(Vector3f position, Quaternionf rotation, Vector3f scale) {
		transformationMatrix.identity().translate(position).
				rotateX((float)Math.toRadians(-rotation.x)).
				rotateY((float)Math.toRadians(-rotation.y)).
				rotateZ((float)Math.toRadians(-rotation.z)).
				scale(scale);

		this.mesh.vertexBuffer.rewind();

		for (TextCharacter character : textCharacters) {
			character.updateMeshPart(new Matrix4f(transformationMatrix), this.mesh.vertexBuffer);
		}

		this.mesh.updateVertexData();
	}

	public void bind() {
		this.mesh.bind();
		this.resizeBuffer();
	}

	public void render() {
		this.mesh.render();
	}

	public void destroy() {
		this.mesh.destroy();
	}

	public void setWidth(int width) {
		this.width = width;
		//TODO re-sort lines
	}
}
