package com.terminalvelocitycabbage.engine.prefabs.gameobjects.models;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.prefabs.gameobjects.shapes.LineShapes;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Collections;

public class BoxLineModel extends Model {
    public BoxLineModel(Vector3f origin, float xDim, float yDim, float zDim, Vector4f color) {
        super(RenderFormat.POSITION_COLOUR, new RenderMode(RenderMode.Modes.LINES), Collections.singletonList(
                new Model.Part(LineShapes.createLineBox(
                        Vertex.positionColour(origin.x, origin.y + yDim, origin.z + zDim, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z + zDim, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x, origin.y + yDim, origin.z, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x, origin.y, origin.z + zDim, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x + xDim, origin.y, origin.z + zDim, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x + xDim, origin.y, origin.z, 1, 0, 0, 1),
                        Vertex.positionColour(origin.x, origin.y, origin.z, 1, 0, 0, 1)
                ))
        ));
    }
}
