package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Vector3f;

public class ScaleComponent implements Component {

    Vector3f scale;

    @Override
    public void setDefaults() {
        scale = new Vector3f(1);
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }
}
