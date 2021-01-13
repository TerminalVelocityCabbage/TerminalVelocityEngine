package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextCharacter;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMeshPartStorage;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Text {

	boolean needsUpdate;

	public float zIndex = 0.0f;
	//This should be a constant that is created on init in the implementer's game and shared between texts.
	private final FontMeshPartStorage fontMeshPartStorage;
	private String text;
	private TextModel model;
	//TODO add a TextStyle parameter here to allow coloring and things, and maybe make it so we can store a map of FontMeshPartStorage for bold,italic,etc.

	public Text(String text, FontMeshPartStorage fontMeshPartStorage) {
		this.text = text;
		this.fontMeshPartStorage = fontMeshPartStorage;
		this.needsUpdate = true;
		this.model = new TextModel(fontMeshPartStorage.getFontTexture());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.needsUpdate = true;
		//TODO make sure this isn't empty
		this.model.bind();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	public void update(int width) {
		if (needsUpdate) {

			//Update text lines
			if (width != this.model.width) {
				//Create a Mesh for each character
				var characterModelParts = new ArrayList<TextCharacter>();
				int xOffset = 0;
				int yOffset = 0;
				for (char character : text.toCharArray()) {

					//Create a mesh with the correct UV coordinated from the texture atlas
					TextCharacter textCharacter = new TextCharacter(fontMeshPartStorage.getMesh(character));

					//Put the text on the next and reset x position line if it would overflow
					if (xOffset + fontMeshPartStorage.getCharInfo(character).getWidth() > width) {
						yOffset -= fontMeshPartStorage.getFontTexture().getHeight();
						xOffset = 0;
					}

					//Position the character based on the x offset and y offset
					textCharacter.position.set(xOffset, yOffset, zIndex);

					//Add the character model part to the model
					characterModelParts.add(textCharacter);

					//Update the next character's xOffset
					//TODO if this causes issues it's because charWidth is returned in pixels
					xOffset += fontMeshPartStorage.getCharInfo(character).getWidth();
				}
				this.model.textCharacters = characterModelParts;
			}

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
