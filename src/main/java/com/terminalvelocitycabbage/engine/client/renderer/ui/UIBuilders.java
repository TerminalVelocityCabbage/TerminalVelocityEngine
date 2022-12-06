package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.Window;

public class UIBuilders {

    public static class ElementBuilder extends UnsafeElementBuilder<ElementBuilder> {
        public ElementBuilder() { }
    }

    public static class ContainerBuilder extends UnsafeContainerBuilder<ContainerBuilder> {
        public ContainerBuilder() { }
    }

    public static class CanvasBuilder extends UnsafeCanvasBuilder<CanvasBuilder> {
        public CanvasBuilder(Window window) {
            super(window);
        }
    }
}
