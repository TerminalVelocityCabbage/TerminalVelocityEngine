package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;

public class InputSensitivityComponent implements Component {

    float sensitivity;

    @Override
    public void setDefaults() {
        sensitivity = 1f;
    }

    public float getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(float sensitivity) {
        this.sensitivity = sensitivity;
    }
}
