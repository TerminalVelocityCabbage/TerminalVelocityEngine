package com.terminalvelocitycabbage.templates.ecs.systems;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.ecs.ComponentFilter;
import com.terminalvelocitycabbage.engine.ecs.Entity;
import com.terminalvelocitycabbage.engine.ecs.System;
import org.lwjgl.nanovg.NanoVG;

import java.util.List;

public class RenderUISystem extends System {

    @Override
    public List<Entity> getEntities() {
        return getManager().getMatchingEntities(ComponentFilter.builder().anyOf(getManager().getComponentTypesOf("uirenderable")).build());
    }

    @Override
    public void update(float deltaTime) {

        var window = ClientBase.getWindow();

        int width = (int) (window.getEffectiveWidth());
        int height = (int) (window.getEffectiveHeight());

        var vg = ClientBase.getRenderer().getNanoVG();

        NanoVG.nvgBeginFrame(vg, width, height, Math.max(window.getContentScaleX(), window.getContentScaleY()));

        getEntities().forEach(entity -> {
            entity.getComponentTypesWithTag("uirenderable").forEach(componentClass -> {
                if (entity.getComponentUnsafe(componentClass) instanceof  UIRenderable uiRenderable) {
                    uiRenderable.draw(vg);
                }
            });
        });

        NanoVG.nvgEndFrame(vg);
    }
}
