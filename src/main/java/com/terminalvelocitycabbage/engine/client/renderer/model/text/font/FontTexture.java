package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

public class FontTexture {

	FontInfo fontInfo;
	Texture texture;

	public FontTexture(FontInfo info, ResourceManager resourceManager, Identifier identifier) {
		this.fontInfo = info;
		this.texture = new Texture(resourceManager, identifier);
	}

	public void bind() {
		texture.bind();
	}

	public void destroy() {
		texture.destroy();
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	public Texture getTexture() {
		return texture;
	}
}
