package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;

public class TextGameObject extends EmptyGameObject {

	public static final float Z_POS = 0.0f;
	private final FontTexture fontTexture;
	private String text;
	private Model model;

	public TextGameObject(String text, FontTexture texture) {
		super();
		this.text = text;
		this.fontTexture = texture;
		this.model = createTextModel();
	}

	private Model createTextModel() {

		//Create a Mesh for each character
		var characterMeshes = new ArrayList<Mesh>();
		for (char character : text.toCharArray()) {
			characterMeshes.add(buildCharacterMesh(character));
		}

		//Create a Model.Part for each character mesh
		var characterModelParts = new ArrayList<Model.Part>();
		for (Mesh mesh : characterMeshes) {
			Model.Part part = new Model.Part(mesh);
			characterModelParts.add(part);
		}

		//Create a text model from the model parts
		Model model = new Model(characterModelParts);
		Log.info(model.modelParts.size());
		//Set the model's texture to the font's
		model.setMaterial(fontTexture.getTexture().toMaterial());

		//Make sure the part knows it's parent
		for (Model.Part part : model.modelParts) {
			part.setModel(model);
		}

		return model;
	}

	private Mesh buildCharacterMesh(char character) {

		FontTexture.CharInfo charInfo = fontTexture.getCharInfo(character);

		//Top Left vertex
		Vertex topLeft = new Vertex()
				.setXYZ(0, 0, Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 0);
		//Bottom Left vertex
		Vertex bottomLeft = new Vertex()
				.setXYZ(0, (float) fontTexture.getHeight(), Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 1);
		//Bottom Right vertex
		Vertex bottomRight = new Vertex()
				.setXYZ(charInfo.getWidth(), (float) fontTexture.getHeight(), Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 1);
		//Top Right vertex
		Vertex topRight = new Vertex()
				.setXYZ(charInfo.getWidth(), 0, Z_POS)
				.setNormal(0, 1, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 0);

		return new TexturedRectangle(topLeft, bottomLeft, bottomRight, topRight);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.model.destroy();
		this.model = createTextModel();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	@Override
	public void update() {
		if (needsUpdate) {
			model.update(position, rotation, scale);
			needsUpdate = false;
		}
	}

	public void destroy() {
		model.destroy();
	}

	public Model getModel() {
		return model;
	}
}
