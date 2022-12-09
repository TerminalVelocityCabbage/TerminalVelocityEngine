package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.renderer.gameobjects.lights.DirectionalLight;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class DirectionalLightComponent implements Component {

    DirectionalLight directionalLight;

    @Override
    public void setDefaults() {
        directionalLight = null;
    }

    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    public void setDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight = directionalLight;
    }
}
