package com.terminalvelocitycabbage.templates.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.templates.gameobjects.models.LineModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Line extends ModeledGameObject {

    public Line(Vector3f start, Vector3f end, Vector4f color, float thickness) {
        super(new LineModel(
                RenderFormat.POSITION_COLOUR,
                new RenderMode(RenderMode.Modes.LINES, thickness),
                Vertex.positionColour(start.x, start.y, start.z, color.x, color.y, color.z, color.w),
                Vertex.positionColour(end.x, end.y, end.z, color.x, color.y, color.z, color.w)
        ));
        //TODO make it so you don't need a material to do things, default to vertex color n stuff
        getModel().setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
    }

    public Vector3f getStartPoint() {
        float[] vertexPosition = getModel().modelParts.get(0).meshPart.getVertex(0).getXYZ();
        return new Vector3f(vertexPosition[0], vertexPosition[1], vertexPosition[2]);
    }

    public Vector3f getEndPoint() {
        float[] vertexPosition = getModel().modelParts.get(0).meshPart.getVertex(1).getXYZ();
        return new Vector3f(vertexPosition[0], vertexPosition[1], vertexPosition[2]);
    }

    public Vector3f getDirection() {
        return getStartPoint().sub(getEndPoint(), new Vector3f());
    }
}
