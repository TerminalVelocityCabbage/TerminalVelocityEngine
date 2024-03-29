package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.utils.Color;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.PointModel;
import org.joml.Vector3f;

public class PointEntity extends Entity {

    public PointEntity(Manager manager, Vector3f location, float size, Color color) {
        super(manager);
        addComponent(PositionComponent.class).setPosition(location);
        addComponent(RotationComponent.class);
        addComponent(ScaleComponent.class);
        addComponent(ModelComponent.class).setModel(new PointModel(size));
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color).build());
    }

}
