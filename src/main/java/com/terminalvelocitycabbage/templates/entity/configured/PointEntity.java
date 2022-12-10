package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.PointModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class PointEntity extends Entity {

    public PointEntity(Vector3f location, float size, Vector4f color) {
        addComponent(PositionComponent.class).setPosition(location);
        addComponent(RotationComponent.class);
        addComponent(ScaleComponent.class);
        addComponent(ModelComponent.class).setModel(new PointModel(size));
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
    }

}
