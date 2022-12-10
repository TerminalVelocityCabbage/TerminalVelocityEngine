package com.terminalvelocitycabbage.templates.entity.models;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.templates.entity.shapes.LineShapes;

import java.util.Collections;

public class BoxLineModel extends Model {
    public BoxLineModel(float xDim, float yDim, float zDim) {
        super(RenderFormat.POSITION, new RenderMode(RenderMode.Modes.LINES), Collections.singletonList(
                new Model.Part(LineShapes.createLineBox(
                        Vertex.position(0, yDim, zDim),
                        Vertex.position(xDim, yDim, zDim),
                        Vertex.position(xDim, yDim, 0),
                        Vertex.position(0, yDim, 0),
                        Vertex.position(0, 0, zDim),
                        Vertex.position(xDim, 0, zDim),
                        Vertex.position(xDim, 0, 0),
                        Vertex.position(0, 0, 0)
                ))
        ));
    }
}
