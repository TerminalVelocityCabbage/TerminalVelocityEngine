package com.terminalvelocitycabbage.engine.client.renderer.ui.text;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;

import java.util.ArrayList;
import java.util.List;

public class TextModel extends Model {

    public int width;

    public TextModel(FontMaterial material) {
        super(RenderFormat.POSITION_UV, new RenderMode(RenderMode.Modes.TRIANGLES), new ArrayList<>());
        this.setMaterial(material);
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
