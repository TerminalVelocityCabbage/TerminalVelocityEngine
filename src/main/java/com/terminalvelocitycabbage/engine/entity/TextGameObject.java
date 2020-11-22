package com.terminalvelocitycabbage.engine.entity;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.model.Mesh;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TexturedRectangle;
import org.joml.Matrix4f;

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
		createTextModel(true);
		this.transformationMatrix = new Matrix4f();
	}

	private void createTextModel(boolean createNew) {

		//Create a Mesh for each character
		var characterModelParts = new ArrayList<Model.Part>();
		int previousWidth = 0;
		for (char character : text.toCharArray()) {
			//Create a mesh with the correct UV coordinated from the texture atlas
			Model.Part part = new Model.Part(buildCharacterMesh(character));
			//Offset the character to be next to the previous by the previous character's width
			part.position.set(previousWidth, 0, 0);
			//Add the character model part to the model
			characterModelParts.add(part);
			//Update the next character's offset
			previousWidth = fontTexture.getCharInfo(character).getWidth();
			//Make sure the model part knows of it's parent
			part.setModel(model);
		}

		if (createNew) {
			//Create a text model from the model parts
			Model model = new Model(characterModelParts);
			//Set the model's texture to the font's
			model.setMaterial(fontTexture.getTexture().toMaterial());
			this.model = model;
		} else {
			this.model.modelParts = characterModelParts;
		}
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
		createTextModel(false);
		this.model.bind();
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
