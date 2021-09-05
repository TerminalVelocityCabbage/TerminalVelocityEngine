package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.List;

public abstract class UnsafeContainerBuilder<T extends UnsafeContainerBuilder<T>> extends UnsafeUIRenderableBuilder<T, Container> {

    public UIDimension width;
    public UIDimension height;
    public Anchor anchorPoint;

    public Alignment.Horizontal horizontalAlignment;
    public Alignment.Vertical verticalAlignment;
    public Alignment.Direction alignmentDirection;
    public Overflow overflow;
    public Wrap wrap;

    public List<UnsafeContainerBuilder<?>> childContainers;
    public List<UnsafeElementBuilder<?>> elements;

    public UnsafeContainerBuilder() {

        this.width = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.height = new UIDimension(0, UIDimension.Unit.PIXELS);
        this.anchorPoint = new Anchor(AnchorPoint.TOP_LEFT, AnchorDirection.RIGHT_DOWN);
        this.horizontalAlignment = Alignment.Horizontal.LEFT;
        this.verticalAlignment = Alignment.Vertical.TOP;
        this.alignmentDirection = Alignment.Direction.HORIZONTAL;
        this.overflow = Overflow.SHOWN;
        this.wrap = Wrap.WRAP;
        this.childContainers = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.backgroundAlpha = new AnimatableUIValue(0);
    }

    public Container build(UIRenderable<?> parent) {

        if (parent == null) Log.crash("Tried to instantiate Container without parent", new RuntimeException("Unspecified parent value"));

        Container container = new Container(parent);

        container.backgroundRed = backgroundRed;
        container.backgroundGreen = backgroundGreen;
        container.backgroundBlue = backgroundBlue;
        container.backgroundAlpha = backgroundAlpha;
        container.borderRed = borderRed;
        container.borderGreen = borderGreen;
        container.borderBlue = borderBlue;
        container.borderAlpha = borderAlpha;
        container.borderRadius = borderRadius;
        container.borderThickness = borderThickness;
        container.margin = margin;

        container.hoverConsumers = hoverConsumers;
        container.unHoverConsumers = unHoverConsumers;
        container.leftClickConsumers = leftClickConsumers;
        container.rightClickConsumers = rightClickConsumers;
        container.doubleClickConsumers = doubleClickConsumers;

        container.width = width;
        container.height = height;
        container.anchorPoint = anchorPoint;
        container.horizontalAlignment = horizontalAlignment;
        container.verticalAlignment = verticalAlignment;
        container.alignmentDirection = alignmentDirection;
        container.overflow = overflow;
        container.wrap = wrap;

        container.childContainers = new ArrayList<>();
        childContainers.forEach(containerBuilder -> container.childContainers.add(containerBuilder.build(container)));
        container.elements = new ArrayList<>();
        elements.forEach(elementBuilder -> container.elements.add(elementBuilder.build(container)));

        container.bind();

        return container;
    }

    public T dimensions(UIDimension width, UIDimension height) {
        this.width = width;
        this.height = height;
        return this.self;
    }

    public T anchorPoint(Anchor anchorPoint) {
        this.anchorPoint = anchorPoint;
        return this.self;
    }

    public T horizontalAlignment(Alignment.Horizontal horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this.self;
    }

    public T verticalAlignment(Alignment.Vertical verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this.self;
    }

    public T alignmentDirection(Alignment.Direction alignmentDirection) {
        this.alignmentDirection = alignmentDirection;
        return this.self;
    }

    public T overflow(Overflow overflow) {
        this.overflow = overflow;
        return this.self;
    }

    public T wrap(Wrap wrap) {
        this.wrap = wrap;
        return this.self;
    }

    public T addContainer(UnsafeContainerBuilder<?> container) {
        this.childContainers.add(container);
        return this.self;
    }

    public T addElement(UnsafeElementBuilder<?> element) {
        this.elements.add(element);
        return this.self;
    }
}
