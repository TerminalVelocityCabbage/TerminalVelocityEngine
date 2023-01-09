package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.utils.Color;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.LineModel;
import org.joml.Vector3f;

public class LineEntity extends Entity {

    public LineEntity(Manager manager, Vector3f start, Vector3f end, Color color, float thickness) {
        super(manager);
        addComponent(PositionComponent.class);
        addComponent(RotationComponent.class);
        addComponent(ScaleComponent.class);
        addComponent(ModelComponent.class).setModel(new LineModel(start, end, thickness));
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color).build());
    }
}
