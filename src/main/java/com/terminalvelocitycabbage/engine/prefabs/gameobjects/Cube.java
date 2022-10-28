package com.terminalvelocitycabbage.engine.prefabs.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.ModeledGameObject;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.prefabs.gameobjects.models.CuboidModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Cube extends ModeledGameObject {

    public Cube(Vector3f location, Vector4f color, float size) {
        super(new CuboidModel(
                RenderFormat.POSITION_COLOUR,
                new RenderMode(RenderMode.Modes.TRIANGLES, size),
                Vertex.positionColour(location.x - 0.5f, location.y + 0.5f, location.z + 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x + 0.5f, location.y + 0.5f, location.z + 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x + 0.5f, location.y - 0.5f, location.z + 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x - 0.5f, location.y - 0.5f, location.z + 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x - 0.5f, location.y + 0.5f, location.z - 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x + 0.5f, location.y + 0.5f, location.z - 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x + 0.5f, location.y - 0.5f, location.z - 0.5f, color.x, color.y, color.z, color.w),
                Vertex.positionColour(location.x - 0.5f, location.y - 0.5f, location.z - 0.5f, color.x, color.y, color.z, color.w)
        ));
        //TODO make it so you don't need a material to do things, default to vertex color n stuff
        getModel().setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
        scale(size);
    }
}
