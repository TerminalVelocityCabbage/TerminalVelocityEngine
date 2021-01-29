package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import com.terminalvelocitycabbage.engine.client.renderer.model.VertexCounter;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TextRectangle;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class TextCharacter {
	public TextMeshPart meshPart;

	public Vector3f offset;
	public Vector3f position;
	public Quaternionf rotation;
	public Vector3f scale;

	public TextCharacter(TextRectangle part) {
		this.meshPart = new TextMeshPart(part);

		this.offset = new Vector3f();
		this.position = new Vector3f();
		this.rotation = new Quaternionf();
		this.scale = new Vector3f(1);
	}

	public TextCharacter(TextMeshPart meshPart, Vector3f offset, Vector3f position, Quaternionf rotation, Vector3f scale) {
		this.meshPart = meshPart;

		this.offset = offset;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void updateMeshPart(Matrix4f transformationMatrix, FloatBuffer buffer) {
		transformationMatrix
				.translate(position)
				.rotate(rotation);
		transformationMatrix
				.translate(offset)
				.scale(scale);
		this.meshPart.update(transformationMatrix, buffer);
	}

	public int getTotalVertexCount() {
		return this.meshPart.verticesCount();
	}

	public int getTotalIndexCount() {
		return this.meshPart.vertexOrderCount();
	}

	public void allocateMesh(ShortBuffer indexBuffer, VertexCounter counter) {
		this.meshPart.allocate(indexBuffer, counter);
	}
}
