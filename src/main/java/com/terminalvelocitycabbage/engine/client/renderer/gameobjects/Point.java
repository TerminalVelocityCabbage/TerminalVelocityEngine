package com.terminalvelocitycabbage.engine.client.renderer.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.types.PointModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Point extends ModeledGameObject {

    public Point(Vector3f location, Vector4f color, float size) {
        super(new PointModel(
                RenderFormat.POSITION_COLOUR,
                new RenderMode(RenderMode.Modes.POINTS, size),
                Vertex.positionColour(location.x, location.y, location.z, color.x, color.y, color.z, color.w)
        ));
        //TODO make it so you don't need a material to do things, default to vertex color n stuff
        getModel().setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
    }
}
