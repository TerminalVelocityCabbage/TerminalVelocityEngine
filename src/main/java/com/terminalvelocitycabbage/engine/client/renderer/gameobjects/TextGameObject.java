package com.terminalvelocitycabbage.engine.client.renderer.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.font.FontTexture;
import com.terminalvelocitycabbage.engine.client.renderer.model.*;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TextRectangle;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class TextGameObject extends EmptyGameObject {

	public static final float Z_POS = 0.0f;
	private final FontTexture fontTexture;
	private String text;
	private TextModel model;

	public TextGameObject(String text, FontTexture texture) {
		super();
		this.text = text;
		this.fontTexture = texture;
		createTextModel(true);
		this.transformationMatrix = new Matrix4f();
		enable();
	}

	private void createTextModel(boolean createNew) {

		//Create a Mesh for each character
		var characterModelParts = new ArrayList<TextModel.Character>();
		int previousWidth = 0;
		for (char character : text.toCharArray()) {
			//Create a mesh with the correct UV coordinated from the texture atlas
			TextModel.Character part = new TextModel.Character(buildCharacterMesh(character));
			//Offset the character to be next to the previous by the previous character's width
			part.position.set(previousWidth, 0, 0);
			//Add the character model part to the model
			characterModelParts.add(part);
			//Update the next character's offset
			previousWidth = fontTexture.getCharInfo(character).getWidth();
		}

		if (createNew) {
			//Create a text model from the model parts
			TextModel model = new TextModel(characterModelParts);
			//Set the model's texture to the font's
			model.setFontTexture(fontTexture);
			this.model = model;
		} else {
			this.model.characters = characterModelParts;
		}
	}

	private MeshPart buildCharacterMesh(char character) {

		FontTexture.CharInfo charInfo = fontTexture.getCharInfo(character);

		//Top Left vertex
		TextVertex topLeft = new TextVertex()
				.setXYZ(0, 0, Z_POS)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 0);
		//Bottom Left vertex
		TextVertex bottomLeft = new TextVertex()
				.setXYZ(0, (float) fontTexture.getHeight(), Z_POS)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 1);
		//Bottom Right vertex
		TextVertex bottomRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), (float) fontTexture.getHeight(), Z_POS)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 1);
		//Top Right vertex
		TextVertex topRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), 0, Z_POS)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 0);

		return TextRectangle.createTextRectangle(topLeft, bottomLeft, bottomRight, topRight);
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
		if (render) {
			model.render();
		}
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

	public TextModel getModel() {
		return model;
	}
}
