package com.terminalvelocitycabbage.templates.entity.configured;

import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderFormat;
import com.terminalvelocitycabbage.engine.client.renderer.elements.RenderMode;
import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Vertex;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.templates.ecs.components.*;
import com.terminalvelocitycabbage.templates.entity.models.CuboidModel;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CubeEntity extends Entity {

    public CubeEntity(Vector3f location, Vector4f color, Vector3f size) {
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
        addComponent(MaterialComponent.class).setMaterial(Material.builder().color(color.x, color.y, color.z, color.w).build());
    }
}
