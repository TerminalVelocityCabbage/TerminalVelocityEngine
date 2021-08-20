package com.terminalvelocitycabbage.engine.client.renderer.model.text.font;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.client.resources.Identifier;
import com.terminalvelocitycabbage.engine.client.resources.ResourceManager;

public class FontMaterial extends Material {

	FontInfo fontInfo;
	Texture texture;

	public FontMaterial(FontInfo info, ResourceManager resourceManager, Identifier identifier) {
		super(null, DEFAULT_ALBEDO_COLOR, DEFAULT_ALBEDO_COLOR, DEFAULT_ALBEDO_COLOR, 1F, null);
		this.fontInfo = info;
		this.texture = new Texture(resourceManager, identifier);
	}

	@Override
	public boolean hasTexture() {
		return true;
	}

	public FontInfo getFontInfo() {
		return fontInfo;
	}

	@Override
	public Texture getTexture() {
		return texture;
	}
}
