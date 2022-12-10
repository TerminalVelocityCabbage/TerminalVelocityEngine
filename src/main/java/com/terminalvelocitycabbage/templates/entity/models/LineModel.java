package com.terminalvelocitycabbage.templates.entity.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.templates.entity.shapes.LineShapes;
import org.joml.Vector3f;

import java.util.Collections;

public class LineModel extends Model {

    public LineModel(Vector3f start, Vector3f end, float thickness) {
        super(RenderFormat.POSITION, new RenderMode(RenderMode.Modes.LINES, thickness), Collections.singletonList(new Model.Part(
                LineShapes.createLine(Vertex.position(start.x, start.y, start.z), Vertex.position(end.x, end.y, end.z))
        )));
    }

}
