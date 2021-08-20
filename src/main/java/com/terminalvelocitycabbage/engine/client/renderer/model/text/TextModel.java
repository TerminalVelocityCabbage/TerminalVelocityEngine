package com.terminalvelocitycabbage.engine.client.renderer.model.text;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.text.font.FontMaterial;

import java.util.Collections;
import java.util.List;

public class TextModel extends Model {

    public int width;

    public TextModel(FontMaterial material) {
        super(RenderFormat.POSITION_UV_COLOUR, Collections.emptyList());
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setCharacters(List<Model.Part> parts) {
        this.modelParts.clear();
        this.modelParts.addAll(parts);
        this.onPartsChange();
    }

    public int getWidth() {
        return width;
    }
}
