package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;

public class ElementBuilder<T extends ElementBuilder<T>> extends UIRenderableBuilder<T> {

    public UIDimension width;
    public UIDimension height;
    public Text innerText;

    public ElementBuilder() {

        this.width = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.height = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.innerText = Text.EMPTY;
    }

    public Element build(Container parent) {

        Element element = new Element(parent);

        element.width = width;
        element.height = height;
        element.innerText = innerText;

        element.bind();

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
