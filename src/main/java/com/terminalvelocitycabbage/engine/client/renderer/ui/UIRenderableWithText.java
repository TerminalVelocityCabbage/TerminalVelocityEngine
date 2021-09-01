package com.terminalvelocitycabbage.engine.client.renderer.ui;

public abstract class UIRenderableWithText<T extends UIRenderableWithText> extends UIRenderable<UIRenderableWithText> {

    public UIRenderableWithText() {
    }

    public abstract void renderText();

    public abstract Text getText();
}
