package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.text.TextVertex;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.TextRectangle;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

public class FontMeshPartStorage {

	FontTexture fontTexture;
	Map<Character, TextRectangle> characterMeshParts;

	public FontMeshPartStorage(FontTexture fontTexture) {
		this.fontTexture = fontTexture;
		this.characterMeshParts = new HashMap<>();
		fontTexture.fontInfo.getCharMap().forEach((character, charInfo) -> characterMeshParts.put(character, buildCharacterMesh(charInfo)));
	}

	public TextRectangle getMesh(char character) {
		if (!characterMeshParts.containsKey(character)) {
			Log.crash("Font Read Error", new RuntimeException("Character not found in mesh part storage for " + character));
		}
		return characterMeshParts.get(character);
	}

	public CharInfo getCharInfo(char character) {
		if (fontTexture.fontInfo.getCharInfo(character) == null) {
			Log.crash("Font Read Error", new RuntimeException("Character not found in mesh part storage for " + character));
		}
		return fontTexture.fontInfo.getCharInfo(character);
	}

	public FontTexture getFontTexture() {
		return fontTexture;
	}

	private TextRectangle buildCharacterMesh(CharInfo charInfo) {

		//Top Left vertex
		TextVertex topLeft = new TextVertex()
				.setXYZ(0, 0, 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.texture.getWidth(), 0);
		//Bottom Left vertex
		TextVertex bottomLeft = new TextVertex()
				.setXYZ(0, (float) fontTexture.texture.getHeight(), 0)
				.setUv((float) charInfo.getStartX() / (float) fontTexture.texture.getWidth(), 1);
		//Bottom Right vertex
		TextVertex bottomRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), (float) fontTexture.texture.getHeight(), 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.texture.getWidth(), 1);
		//Top Right vertex
		TextVertex topRight = new TextVertex()
				.setXYZ(charInfo.getWidth(), 0, 0)
				.setUv((float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.texture.getWidth(), 0);

		return TextRectangle.createTextRectangle(topLeft, bottomLeft, bottomRight, topRight);
	}
}
