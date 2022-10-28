package com.terminalvelocitycabbage.engine.prefabs.gameobjects.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.prefabs.gameobjects.shapes.LineShapes;

import java.util.Collections;

public class LineModel extends Model {

    public LineModel(RenderFormat format, RenderMode mode, Vertex start, Vertex end) {
        super(format, mode, Collections.singletonList(new Model.Part(
                LineShapes.createLine(start, end)
        )));
    }

}
