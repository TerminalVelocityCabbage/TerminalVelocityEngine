package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextCharacter;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMeshPartStorage;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Text {

	boolean needsUpdate;

	public float zIndex = 0.0f;
	//This should be a constant that is created on init in the implementer's game and shared between texts.
	private final FontMeshPartStorage fontMeshPartStorage;
	private String text;
	//todo make private
	public TextModel model;
	//TODO add a TextStyle parameter here to allow coloring and things, and maybe make it so we can store a map of FontMeshPartStorage for bold,italic,etc.

	public Text(String text, FontMeshPartStorage fontMeshPartStorage) {
		this.text = text;
		this.fontMeshPartStorage = fontMeshPartStorage;
		this.needsUpdate = true;
		this.model = new TextModel(fontMeshPartStorage.getFontTexture());
		this.model.resizeBuffer();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.needsUpdate = true;
		//TODO make sure this isn't empty
		this.model.resizeBuffer();
		this.model.bind();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	public void update(int lineWidth, Window window) {
		if (needsUpdate) {
			//If we wanted to do font size, we can just replace 1F here
			float xScale = 1F / window.width();
			float yScale = 1F / window.height();

			//Update text lines
			if (lineWidth != this.model.width) {
				//Create a Mesh for each character
				var characterModelParts = new ArrayList<TextCharacter>();
				int xOffset = 0;
				int yOffset = 0;
				boolean t = false;
				for (char character : text.toCharArray()) {
					//Create a mesh with the correct UV coordinated from the texture atlas
					TextCharacter textCharacter = new TextCharacter(fontMeshPartStorage.getMesh(character));

					//Put the text on the next and reset x position line if it would overflow
					if (xOffset + fontMeshPartStorage.getCharInfo(character).getWidth() > lineWidth) {
						yOffset -= fontMeshPartStorage.getFontTexture().getHeight();
						xOffset = 0;
					}

					//Position the character based on the x offset and y offset
					textCharacter.offset.set(xOffset*xScale - 1F, yOffset*yScale + 1F, zIndex);
					textCharacter.scale.set(xScale, -yScale, 1);
					//Add the character model part to the model
					characterModelParts.add(textCharacter);

					//Update the next character's xOffset
					//TODO if this causes issues it's because charWidth is returned in pixels
					xOffset += fontMeshPartStorage.getCharInfo(character).getWidth();
				}
				this.model.textCharacters = characterModelParts;
			}

			model.resizeBuffer();
			model.update(new Vector3f(1F, -1F, zIndex), new Quaternionf(), new Vector3f(1));
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
