package com.terminalvelocitycabbage.engine.client.renderer.model.types;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.shapes.LineShape;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class BoxLineModel extends Model {
    public BoxLineModel(Vector3f origin, float xDim, float yDim, float zDim, Vector4f color) {
        super(RenderFormat.POSITION_COLOUR, new RenderMode(RenderMode.Modes.LINES),
                List.of(
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x + xDim, origin.y, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y + yDim, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y, origin.z + zDim, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z + zDim, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x - xDim, origin.y, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z + zDim, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y - yDim, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z + zDim, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y, origin.z - zDim, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y, origin.z + zDim, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x + xDim, origin.y, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y, origin.z + zDim, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y + yDim, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x - xDim, origin.y, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y + yDim, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y - yDim, origin.z, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x + xDim, origin.y, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y, origin.z + zDim, color.x, color.y, color.z, color.w))),
                new Model.Part(LineShape.createLine(Vertex.positionColour(origin.x, origin.y + yDim, origin.z, color.x, color.y, color.z, color.w), Vertex.positionColour(origin.x, origin.y, origin.z + zDim, color.x, color.y, color.z, color.w)))
        ));
    }
}
