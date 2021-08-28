package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;

public abstract class UIRenderableWithText extends UIRenderable {

    public UIRenderableWithText(Style style) {
        super(style);
    }

    public abstract void renderText();

    public abstract Text getText();
}
