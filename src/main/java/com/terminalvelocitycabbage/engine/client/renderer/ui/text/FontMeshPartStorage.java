package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.model.MeshPart;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.HashMap;
import java.util.Map;

public class FontMeshPartStorage {

	final FontMaterial fontMaterial;
	final Map<Character, MeshPart> characterMeshParts;

	public FontMeshPartStorage(FontMaterial material) {
		this.fontMaterial = material;
		this.characterMeshParts = new HashMap<>();
		material.fontInfo.getCharMap().forEach((character, charInfo) -> characterMeshParts.put(character, buildCharacterMesh(charInfo)));
	}

	public MeshPart getMesh(char character) {
		if (!characterMeshParts.containsKey(character)) {
			Log.crash("Font Read Error", new RuntimeException("Character not found in mesh part storage for " + character));
		}
		return characterMeshParts.get(character).deepCopy();
	}

	public CharInfo getCharInfo(char character) {
		if (fontMaterial.fontInfo.getCharInfo(character) == null) {
			Log.crash("Font Read Error", new RuntimeException("Character not found in mesh part storage for " + character));
		}
		return fontMaterial.fontInfo.getCharInfo(character);
	}

	public FontMaterial getFontMaterial() {
		if (fontMaterial == null) {
			Log.crash("No Material Applied to Font Mesh", new RuntimeException("Font Material is Null"));
		}
		return fontMaterial;
	}

	private MeshPart buildCharacterMesh(CharInfo charInfo) {

		Texture fontTexture = getFontMaterial().getTexture();

		Vertex topLeft = Vertex.positionUv(0, 0, 0, (float) charInfo.getStartX() / (float) fontTexture.getWidth(), 0);
		Vertex bottomLeft = Vertex.positionUv(0, (float) fontTexture.getHeight(), 0, (float) charInfo.getStartX() / (float) fontTexture.getWidth(), 1);
		Vertex bottomRight = Vertex.positionUv(charInfo.getWidth(), (float) fontTexture.getHeight(), 0, (float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 1);
		Vertex topRight = Vertex.positionUv(charInfo.getWidth(), 0, 0, (float) (charInfo.getStartX() + charInfo.getWidth()) / (float) fontTexture.getWidth(), 0);

		return new MeshPart(
			new Vertex[]{topLeft, bottomLeft, bottomRight, topRight},
			new int[]{ 0, 1, 2, 2, 3, 0 }
		);
	}
}
