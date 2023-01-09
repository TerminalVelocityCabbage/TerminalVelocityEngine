package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.renderer.model.Material;
import com.terminalvelocitycabbage.engine.client.renderer.model.Texture;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class MaterialComponent implements Component {

    Material material;

    @Override
    public void setDefaults() {
        material = Material.builder().build();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setMaterialWithTexture(ResourceManager resourceManager, Identifier resourceLocation) {
        this.material = Material.builder().texture(new Texture(resourceManager, resourceLocation)).build();
    }

    @Override
    public void cleanup() {
        if (material.hasTexture()) material.getTexture().destroy();
    }
}
