package com.terminalvelocitycabbage.engine.client.renderer.ui;

public abstract class UIRenderableWithText extends UIRenderable {

    public UIRenderableWithText() {
    }

    public abstract void renderText();

    public abstract Text getText();
}
