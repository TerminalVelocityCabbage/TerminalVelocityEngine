package com.terminalvelocitycabbage.templates.entity.models;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.templates.entity.shapes.LineShapes;
import org.joml.Vector3f;

import java.util.Collections;

public class LineModel extends Model {

    public LineModel(Vector3f start, Vector3f end, float thickness) {
        super(RenderFormat.POSITION, new RenderMode(RenderMode.Modes.LINES, thickness), Collections.singletonList(new Model.Part(
                LineShapes.createLine(Vertex.position(start.x, start.y, start.z), Vertex.position(end.x, end.y, end.z))
        )));
    }

    public Vector3f getStartPoint() {
        float[] vertexPosition = modelParts.get(0).meshPart.getVertex(0).getXYZ();
        return new Vector3f(vertexPosition[0], vertexPosition[1], vertexPosition[2]);
    }

    public Vector3f getEndPoint() {
        float[] vertexPosition = modelParts.get(0).meshPart.getVertex(1).getXYZ();
        return new Vector3f(vertexPosition[0], vertexPosition[1], vertexPosition[2]);
    }

    public Vector3f getDirection() {
        return getStartPoint().sub(getEndPoint(), new Vector3f());
    }
}
