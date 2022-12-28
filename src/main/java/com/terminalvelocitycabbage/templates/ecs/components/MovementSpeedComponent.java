package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.ecs.Component;

public class MovementSpeedComponent implements Component {

    float speed;

    @Override
    public void setDefaults() {
        speed = 1f;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
