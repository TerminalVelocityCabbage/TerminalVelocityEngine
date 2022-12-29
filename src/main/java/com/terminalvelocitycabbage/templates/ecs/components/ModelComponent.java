package com.terminalvelocitycabbage.templates.ecs.components;

import com.terminalvelocitycabbage.engine.client.renderer.model.Model;
import com.terminalvelocitycabbage.engine.client.renderer.model.loader.AnimatedModelLoader;
import com.terminalvelocitycabbage.engine.resources.Identifier;
import com.terminalvelocitycabbage.engine.resources.ResourceManager;
import com.terminalvelocitycabbage.engine.ecs.Component;

public class ModelComponent implements Component {

    Model model;

    @Override
    public void setDefaults() {
        model = null;
    }

    public void setModel(ResourceManager resourceManager, Identifier resourceLocation) {
        model = AnimatedModelLoader.load(resourceManager, resourceLocation);
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void replaceModel(Model model) {
        this.model.destroy();
        this.model = model;
    }

    public Model getModel() {
        return model;
    }
}
