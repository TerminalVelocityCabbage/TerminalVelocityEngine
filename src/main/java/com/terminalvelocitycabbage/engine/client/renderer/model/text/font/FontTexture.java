package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.Resource;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTBakedChar;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.stbtt_BakeFontBitmap;

public class FontTexture {

	private static final float DEFAULT_FONT_HEIGHT = 24f;

	private Map<Character, CharInfo> charMap;

	private int textureID;

	ByteBuffer texture;

	int width;
	int height;

	public FontTexture(ResourceManager resourceManager, Identifier identifier) {
		this(resourceManager, identifier, DEFAULT_FONT_HEIGHT);
	}

	public FontTexture(ResourceManager resourceManager, Identifier identifier, float fontHeight) {
		Optional<Resource> resource = resourceManager.getResource(identifier);
		if (resource.isPresent()) {
			charMap = new HashMap<>();
			ByteBuffer source = resource.get().asByteBuffer().orElseThrow();

			textureID = glGenTextures();
			STBTTBakedChar.Buffer charData = STBTTBakedChar.malloc(96); //Not sure why it's 96

			texture = BufferUtils.createByteBuffer(512 * 512); //512 is base dimension might need scaled or something
			stbtt_BakeFontBitmap(source, fontHeight, texture, 512, 512, 32, charData);

			for (STBTTBakedChar charDatum : charData) {
				charMap.put('a', new CharInfo(charDatum.x0(), charDatum.x1() - charDatum.x0()));
			}

			width = 512;
			height = 512;
		} else {
			throw new RuntimeException("Could not load font resource " + identifier.toString());
		}
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}

	public void destroy() {
		glDeleteTextures(textureID);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public CharInfo getCharInfo(char c) {
		return charMap.get(c);
	}

	public Map<Character, CharInfo> getCharMap() {
		return charMap;
	}
}
