package com.terminalvelocitycabbage.engine.client.renderer.ui.elements;

import com.terminalvelocitycabbage.engine.client.renderer.ui.Element;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Text;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UIRenderableWithText;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;

import java.util.function.Consumer;

public class ButtonElement extends Element {

    public ButtonElement(UIDimension width, UIDimension height, Consumer<UIRenderableWithText> clickEvent,
                         float hoverColorR, float hoverColorG, float hoverColorB, float hoverColorAlpha,
                         Text text) {
        super(width, height);
        this.onClick(clickEvent);
        this.onHover(uiRenderable -> uiRenderable.color(hoverColorR, hoverColorG, hoverColorB, hoverColorAlpha));
        this.onUnHover(UIRenderable::resetColor);
        this.setInnerText(text);
    }
}
