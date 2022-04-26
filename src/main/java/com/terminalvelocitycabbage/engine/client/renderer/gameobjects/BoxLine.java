package com.terminalvelocitycabbage.engine.client.renderer.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.types.BoxLineModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BoxLine extends ModeledGameObject {

    public BoxLine(Vector3f origin, float xDim, float yDim, float zDim, Vector4f color) {
        super(new BoxLineModel(
                origin, xDim, yDim, zDim, color
        ));
        //TODO make it so you don't need a material to do things, default to vertex color n stuff
        getModel().setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
    }

}
