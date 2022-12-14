package com.terminalvelocitycabbage.templates.entity.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.templates.entity.shapes.PointShape;

import java.util.Collections;

public class PointModel extends Model {

    public PointModel(float size) {
        super(RenderFormat.POSITION, new RenderMode(RenderMode.Modes.POINTS, size), Collections.singletonList(new Model.Part(PointShape.createPoint(Vertex.position(0, 0, 0)))));
    }
}
