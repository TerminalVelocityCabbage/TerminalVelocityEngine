package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextCharacter;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextModel;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMeshPartStorage;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Text {

	public static final Text EMPTY = new Text();

	private boolean needsUpdate;
	private FontMeshPartStorage fontMeshPartStorage;
	private String text;
	//todo make private
	public TextModel model;
	//TODO add a TextStyle parameter here to allow coloring and things, and maybe make it so we can store a map of FontMeshPartStorage for bold,italic,etc.

	private Text() {
		this.needsUpdate = true;
		this.text = null;
		this.fontMeshPartStorage = null;
		this.model = null;
	}

	public Text(String text, FontMeshPartStorage fontMeshPartStorage) {
		this.needsUpdate = true;
		this.text = text;
		this.fontMeshPartStorage = fontMeshPartStorage;
		this.model = new TextModel(fontMeshPartStorage.getFontTexture());
		this.model.resizeBuffer();
	}

	public String getText() {
		return text;
	}

	public void setText(Text text) {
		this.fontMeshPartStorage = text.fontMeshPartStorage;
		this.text = text.text;
		this.model = new TextModel(fontMeshPartStorage.getFontTexture());
		this.model.resizeBuffer();
		bind();
	}

	public void setTextString(String text) {
		this.text = text;
		this.model.resizeBuffer();
		bind();
		this.needsUpdate = true;
	}

	public void setFont(FontMeshPartStorage font) {
		this.fontMeshPartStorage = font;
		this.model = new TextModel(fontMeshPartStorage.getFontTexture());
		this.model.resizeBuffer();
		this.bind();
	}

	public void bind() {
		model.bind();
	}

	public void render() {
		model.render();
	}

	public void update(int lineWidth, Window window, float xCenter, float yCenter) {

		if (needsUpdate) {
			//Divided by the window dimensions to make it the correct scale
			//2f because glfw is -1 to 1 (delta 2)
			float xScale = 2f / window.width();
			float yScale = 2f / window.height();

			//Update text lines
			if (lineWidth != this.model.width) {
				//Create a Mesh for each character
				var characterModelParts = new ArrayList<TextCharacter>();
				int xOffset = 0;
				int yOffset = 0;
				for (char character : text.toCharArray()) {
					//Create a mesh with the correct UV coordinated from the texture atlas
					TextCharacter textCharacter = new TextCharacter(fontMeshPartStorage.getMesh(character));

					//Put the text on the next and reset x position line if it would overflow
					if (xOffset + fontMeshPartStorage.getCharInfo(character).getWidth() > lineWidth) {
						yOffset -= fontMeshPartStorage.getFontTexture().getTexture().getHeight();
						xOffset = 0;
					}

					//Position the character based on the x offset and y offset
					textCharacter.offset.set(xOffset * xScale, yOffset * yScale, 0);
					//Y scale is negative because for some reason it's upside down otherwise
					textCharacter.scale.set(xScale, -yScale, 1);
					//Add the character model part to the model
					characterModelParts.add(textCharacter);

					//Update the next character's xOffset
					xOffset += fontMeshPartStorage.getCharInfo(character).getWidth();
				}
				this.model.textCharacters = characterModelParts;
			}

			model.resizeBuffer();
			model.update(new Vector3f(xCenter, yCenter, 0), new Quaternionf(), new Vector3f(1));
		}
	}

	public void destroy() {
		model.destroy();
	}

	public TextModel getModel() {
		return model;
	}
}
