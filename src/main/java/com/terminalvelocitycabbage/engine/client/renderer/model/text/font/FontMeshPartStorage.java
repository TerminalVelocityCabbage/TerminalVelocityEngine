package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

public class FontMeshPartStorage {

	FontMaterial fontTexture;
	Map<Character, MeshPart> characterMeshParts;

	public FontMeshPartStorage(FontMaterial fontTexture) {
		this.fontTexture = fontTexture;
		this.characterMeshParts = new HashMap<>();
		fontTexture.fontInfo.getCharMap().forEach((character, charInfo) -> characterMeshParts.put(character, buildCharacterMesh(charInfo)));
	}

	public MeshPart getMesh(char character) {
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

	public FontMaterial getFontTexture() {
		return fontTexture;
	}

	private MeshPart buildCharacterMesh(CharInfo charInfo) {

		Vertex topLeft = Vertex.positionUvColour(0, 0, 0, (float) charInfo.getStartX() / (float) fontTexture.texture.getWidth(), 0, 1, 1, 1, 1);
		Vertex bottomLeft = Vertex.positionUvColour(0, (float) fontTexture.texture.getHeight(), 0, (float) charInfo.getStartX() / (float) fontTexture.texture.getWidth(), 1, 1, 1, 1, 1);
		Vertex bottomRight = Vertex.positionUvColour(charInfo.getWidth(), (float) fontTexture.texture.getHeight(), 0, (float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.texture.getWidth(), 1, 1, 1, 1, 1);
		Vertex topRight = Vertex.positionUvColour(charInfo.getWidth(), 0, 0, (float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.texture.getWidth(), 0, 1, 1, 1, 1);

		return new MeshPart(
			new Vertex[]{topLeft, bottomLeft, bottomRight, topRight},
			new short[]{ 0, 1, 2, 2, 3, 0 }
		);
	}
}
