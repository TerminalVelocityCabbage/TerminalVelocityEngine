package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextCharacter;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMeshPartStorage;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontTexture;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;

public class Text {

	boolean needsUpdate;

	public float zIndex = 0.0f;
	private final FontMeshPartStorage fontMeshPartStorage;
	private String text;
	private TextModel model;

	public Text(String text, FontTexture texture) {
		this.needsUpdate = false;
		this.text = text;
		this.fontMeshPartStorage = new FontMeshPartStorage(texture);
		this.model = new TextModel(Collections.emptyList(), fontMeshPartStorage.getFontTexture());
		buildCharacterMap();
	}

	private void buildCharacterMap() {
		//Create a Mesh for each character
		var characterModelParts = new ArrayList<TextCharacter>();
		int previousWidth = 0;
		for (char character : text.toCharArray()) {
			//Create a mesh with the correct UV coordinated from the texture atlas
			TextCharacter textCharacter = new TextCharacter(fontMeshPartStorage.getMesh(character));
			//Offset the character to be next to the previous by the previous character's width
			textCharacter.position.set(previousWidth, 0, zIndex);
			//Add the character model part to the model
			characterModelParts.add(textCharacter);
			//Update the next character's offset
			//TODO if this causes issues it's because charWidth is returned in pixels
			previousWidth = fontMeshPartStorage.getCharInfo(character).getWidth();
		}
		this.model.characters = characterModelParts;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		buildCharacterMap();
		this.model.bind();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	public void update() {
		if (needsUpdate) {
			model.update(new Vector3f(0, 0, zIndex), new Quaternionf(), new Vector3f(1));
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
