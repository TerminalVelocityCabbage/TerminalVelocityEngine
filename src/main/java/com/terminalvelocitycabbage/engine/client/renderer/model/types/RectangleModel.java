package com.terminalvelocitycabbage.engine.client.renderer.model.types;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.RectangleShape;

import java.util.Collections;

public class RectangleModel extends Model {

    public RectangleModel(RenderFormat format, Vertex v1, Vertex v2, Vertex v3, Vertex v4) {
        super(format, new RenderMode(RenderMode.Modes.TRIANGLES), Collections.singletonList(new Model.Part(
                RectangleShape.createRectangle(v1, v2, v3, v4)
        )));
    }
}
