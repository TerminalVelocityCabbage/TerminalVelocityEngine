package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextVertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TextRectangle;

import java.util.HashMap;
import java.util.Map;

public class FontMeshPartStorage {

	FontTexture fontTexture;
	Map<Character, TextRectangle> characterMeshParts;

	public FontMeshPartStorage(FontTexture fontTexture) {
		this.fontTexture = fontTexture;
		this.characterMeshParts = new HashMap<>();
		fontTexture.getCharMap().forEach((character, charInfo) -> characterMeshParts.put(character, buildCharacterMesh(charInfo)));
	}

	public TextRectangle getMesh(char character) {
		if (!characterMeshParts.containsKey(character)) {
			throw new RuntimeException("Character not found in mesh part storage for " + character);
		}
		return characterMeshParts.get(character);
	}

	public CharInfo getCharInfo(char character) {
		if (fontTexture.getCharInfo(character) == null) {
			throw new RuntimeException("Character not found in mesh part storage for " + character);
		}
		return fontTexture.getCharInfo(character);
	}

	public FontTexture getFontTexture() {
		return fontTexture;
	}

	private TextRectangle buildCharacterMesh(CharInfo charInfo) {

		//Top Left vertex
		TextVertex topLeft = new TextVertex()
				.setXYZ(0, 0, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 0);
		//Bottom Left vertex
		TextVertex bottomLeft = new TextVertex()
				.setXYZ(0, (float) fontTexture.getHeight(), 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.getWidth(), 1);
		//Bottom Right vertex
		TextVertex bottomRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), (float) fontTexture.getHeight(), 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 1);
		//Top Right vertex
		TextVertex topRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), 0, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 0);

		return TextRectangle.createTextRectangle(topLeft, bottomLeft, bottomRight, topRight);
	}
}
