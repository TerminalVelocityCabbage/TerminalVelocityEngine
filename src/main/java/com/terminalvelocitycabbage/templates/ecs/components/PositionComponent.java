package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Vector3f;

public class PositionComponent implements Component {

    Vector3f position;

    @Override
    public void setDefaults() {
        position = new Vector3f(0, 0, 0);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }
}
