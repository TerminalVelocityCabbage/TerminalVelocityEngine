package com.terminalvelocitycabbage.templates.gameobjects.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.templates.gameobjects.shapes.PointShape;

import java.util.Collections;

public class PointModel extends Model {

    public PointModel(RenderFormat format, RenderMode mode, Vertex point) {
        super(format, mode, Collections.singletonList(new Model.Part(PointShape.createPoint(point))));
    }
}
