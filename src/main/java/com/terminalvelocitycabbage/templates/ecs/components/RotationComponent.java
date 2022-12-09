package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;
import org.joml.Quaternionf;

public class RotationComponent implements Component {

    Quaternionf rotation;

    @Override
    public void setDefaults() {
        rotation = new Quaternionf();
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public void setRotation(Quaternionf rotation) {
        this.rotation = rotation;
    }
}
