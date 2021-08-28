package com.terminalvelocitycabbage.engine.client.renderer.ui.elements;

import com.terminalvelocitycabbage.engine.client.renderer.ui.Element;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import org.joml.Vector4f;

import java.util.function.Consumer;

public class ButtonElement extends Element {

    Vector4f color;
    Vector4f hoverColor;
    Consumer<UIRenderable> clickEvent;

    @Override
    public void callClick() {
        clickEvent.accept(this);
    }

    public ButtonElement(UIDimension width, UIDimension height, Style style, Consumer<UIRenderable> clickEvent, Vector4f color, Vector4f hoverColor) {
        super(width, height, style);
        this.clickEvent = clickEvent;
        this.style.setColor(color.x, color.y, color.z, color.w);
        this.color = color;
        this.hoverColor = hoverColor;
        this.onHover(uiRenderable -> uiRenderable.style.setColor(hoverColor.x, hoverColor.y, hoverColor.z, hoverColor.w));
        this.onUnHover(uiRenderable -> uiRenderable.style.setColor(color.x, color.y, color.z, color.w));
    }
}
