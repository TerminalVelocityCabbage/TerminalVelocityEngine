package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.Manager;
import com.terminalvelocitycabbage.engine.utils.Color;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.CuboidModel;
import org.joml.Vector3f;

public class CubeEntity extends Entity {

    public CubeEntity(Manager manager, Vector3f location, Color color, Vector3f size) {
        super(manager);
        addComponent(PositionComponent.class).setPosition(location);
        addComponent(RotationComponent.class);
        addComponent(ScaleComponent.class).setScale(size);
        addComponent(ModelComponent.class).setModel(new CuboidModel(
                RenderFormat.POSITION,
                new RenderMode(RenderMode.Modes.TRIANGLES),
                Vertex.position(location.x - 0.5f, location.y + 0.5f, location.z - 0.5f),
                Vertex.position(location.x + 0.5f, location.y + 0.5f, location.z - 0.5f),
                Vertex.position(location.x + 0.5f, location.y - 0.5f, location.z - 0.5f),
                Vertex.position(location.x - 0.5f, location.y - 0.5f, location.z - 0.5f),
                Vertex.position(location.x - 0.5f, location.y + 0.5f, location.z + 0.5f),
                Vertex.position(location.x + 0.5f, location.y + 0.5f, location.z + 0.5f),
                Vertex.position(location.x + 0.5f, location.y - 0.5f, location.z + 0.5f),
                Vertex.position(location.x - 0.5f, location.y - 0.5f, location.z + 0.5f)
        ));
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color).build());
    }
}
