package com.terminalvelocitycabbage.engine.client.renderer.ui.elements;

import com.terminalvelocitycabbage.engine.client.renderer.ui.Element;
import com.terminalvelocitycabbage.engine.client.renderer.ui.Text;
import com.terminalvelocitycabbage.engine.client.renderer.ui.UIRenderable;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Style;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;
import org.joml.Vector4f;

import java.util.function.Consumer;

public class ButtonElement extends Element {

    Vector4f color;
    Vector4f hoverColor;

    public ButtonElement(UIDimension width, UIDimension height, Style style, Consumer<UIRenderable> clickEvent, Vector4f hoverColor, Text text) {
        super(width, height, style);
        this.hoverColor = hoverColor;
        this.color = style.getColor(); //Backup the color so we can change it and stuff.
        this.onClick(clickEvent);
        this.onHover(uiRenderable -> uiRenderable.style.setColor(hoverColor));
        this.onUnHover(uiRenderable -> uiRenderable.style.setColor(color));
        this.setInnerText(text);
    }
}
