package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.utils.Color;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.BoxLineModel;
import org.joml.Vector3f;

public class BoxLineEntity extends Entity {

    public BoxLineEntity(Manager manager, Vector3f origin, float xDim, float yDim, float zDim, Color color) {
        super(manager);
        addComponent(PositionComponent.class).setPosition(origin);
        addComponent(RotationComponent.class);
        addComponent(ScaleComponent.class);
        addComponent(ModelComponent.class).setModel(new BoxLineModel(xDim, yDim, zDim));
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color).build());
    }

}
