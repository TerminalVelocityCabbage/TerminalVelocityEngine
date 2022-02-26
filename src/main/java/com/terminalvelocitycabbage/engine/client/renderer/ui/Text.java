package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.FontMeshPartStorage;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.TextModel;
import com.terminalvelocitycabbage.engine.client.renderer.ui.text.TextStyle;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

public class Text {

	public static final Text EMPTY = new Text();

	private boolean needsUpdate;
	private FontMeshPartStorage fontMeshPartStorage; //TODO expand this to be able to store bold/italic etc.
	private String textString;
	private TextModel model;
	private TextStyle style;

	private boolean bound = false;

	private Text() {
		this.needsUpdate = true;
		this.textString = "";
		this.fontMeshPartStorage = null;
		this.model = null;
	}

	public Text(String text, FontMeshPartStorage fontMeshPartStorage) {
		this.needsUpdate = true;
		this.textString = text;
		this.fontMeshPartStorage = fontMeshPartStorage;
		this.style = new TextStyle();
		this.model = new TextModel(fontMeshPartStorage.getFontMaterial());
		this.model.resizeBuffer();
	}

	public Text(String text, FontMeshPartStorage fontMeshPartStorage, TextStyle style) {
		this.needsUpdate = true;
		this.textString = text;
		this.fontMeshPartStorage = fontMeshPartStorage;
		this.style = style;
		this.model = new TextModel(fontMeshPartStorage.getFontMaterial());
		//bind();
		//TODO this crashes the game because it's not bound, but binding it doesn't not crash so error tbd
		if (bound) {
			this.model.resizeBuffer();
		}
	}

	public String getString() {
		return textString;
	}

	public void setTextString(String text) {
		this.textString = text;
		this.model.resizeBuffer();
		bind();
		this.needsUpdate = true;
	}

	public void setFont(FontMeshPartStorage font) {
		this.fontMeshPartStorage = font;
		this.model = new TextModel(fontMeshPartStorage.getFontMaterial());
		this.model.resizeBuffer();
		this.bind();
	}

	public void bind() {
		if (model != null) {
			bound = true;
			model.bind();
		}
	}

	public void render() {
		model.render();
	}

	public void update(int lineWidth, Window window, float xCenter, float yCenter) {

		if (model == null) {
			return;
		}

		if (needsUpdate) {
			//Divided by the window dimensions to make it the correct scale
			//2f because glfw is -1 to 1 (delta 2)
			float xScale = 2f / window.width();
			float yScale = 2f / window.height();

			//Update text lines
			if (lineWidth != this.model.width) {
				//Create a Mesh for each character
				var characterModelParts = new ArrayList<Model.Part>();
				int xOffset = 0;
				int yOffset = 0;
				for (char character : textString.toCharArray()) {
					//Create a mesh with the correct UV coordinated from the texture atlas
					Model.Part textCharacter = new Model.Part(fontMeshPartStorage.getMesh(character));

					//Put the text on the next and reset x position line if it would overflow
					//(style.getSize() / (yCenter * 2)) edits the width of the text to the scaled width based on height values
					if (xOffset + fontMeshPartStorage.getCharInfo(character).getWidth() * (style.getSize() / (yCenter * 2)) > lineWidth) {
						yOffset -= style.getSize();
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
				this.model.setCharacters(characterModelParts);
			}

			model.resizeBuffer();
			if (style != null) {
				model.update(new Vector3f(xCenter, yCenter, 0), new Quaternionf(), new Vector3f(style.getSize() / fontMeshPartStorage.getFontMaterial().getTexture().getHeight()));
			} else {
				model.update(new Vector3f(xCenter, yCenter, 0), new Quaternionf(), new Vector3f(1f));
			}
		}
	}

	public void destroy() {
		model.destroy();
	}

	public TextModel getModel() {
		return model;
	}

	public TextStyle getStyle() {
		return style;
	}
}
