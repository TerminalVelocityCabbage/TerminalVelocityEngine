package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;

public abstract class UnsafeElementBuilder<T extends UnsafeElementBuilder<T>> extends UnsafeUIRenderableBuilder<T, Element> {

    public UIDimension width;
    public UIDimension height;
    public Text innerText;

    public UnsafeElementBuilder() {

        this.width = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.height = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.innerText = Text.EMPTY;
    }

    public Element build(Container parent) {

        Element element = new Element(parent);

        element.backgroundRed = backgroundRed;
        element.backgroundGreen = backgroundGreen;
        element.backgroundBlue = backgroundBlue;
        element.backgroundAlpha = backgroundAlpha;
        element.borderRed = borderRed;
        element.borderGreen = borderGreen;
        element.borderBlue = borderBlue;
        element.borderAlpha = borderAlpha;
        element.borderRadius = borderRadius;
        element.borderThickness = borderThickness;
        element.margin = margin;

        element.hoverConsumers = hoverConsumers;
        element.unHoverConsumers = unHoverConsumers;
        element.leftClickConsumers = leftClickConsumers;
        element.rightClickConsumers = rightClickConsumers;
        element.doubleClickConsumers = doubleClickConsumers;

        element.width = width;
        element.height = height;
        element.innerText = innerText;

//        element.bind();
        innerText.bind();

        return element;
    }

    public T dimensions(UIDimension width, UIDimension height) {
        this.width = width;
        this.height = height;
        return this.self;
    }

    public T innerText(Text innerText) {
        this.innerText = innerText;
        return this.self;
    }
}
