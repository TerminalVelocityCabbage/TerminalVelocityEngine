package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.ClientBase;
import com.terminalvelocitycabbage.engine.client.renderer.components.Window;
import com.terminalvelocitycabbage.engine.events.EventContext;

import java.util.ArrayList;
import java.util.List;

public class CanvasBuilder extends UIRenderableBuilder<Canvas> {

    Window window;
    List<ContainerBuilder> containers;

    public CanvasBuilder(Window window) {
        this.window = window;
        this.containers = new ArrayList<>();
        ClientBase.instance.addEventHandler(EventContext.CLIENT, this);
        this.backgroundAlpha = new AnimatableUIValue(0);
    }

    public Canvas build() {
        Canvas canvas = new Canvas(window);
        canvas.containers = new ArrayList<>();
        containers.forEach(containerBuilder -> canvas.containers.add(containerBuilder.build(canvas)));

        canvas.bind();

        return canvas;
    }

    public void window(Window window) {
        this.window = window;
    }

    public void addContainer(ContainerBuilder container) {
        this.containers.add(container);
    }
}
