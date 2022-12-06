package com.terminalvelocitycabbage.templates.gameobjects;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.ModeledGameObject;
import com.terminalvelocitycabbage.templates.gameobjects.models.BoxLineModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class BoxLine extends ModeledGameObject {

    public BoxLine(Vector3f origin, float xDim, float yDim, float zDim, Vector4f color) {
        super(new BoxLineModel(
                origin, xDim, yDim, zDim, color
        ));
    }

}
