package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.*;
import com.terminalvelocitycabbage.engine.debug.Log;

import java.util.ArrayList;
import java.util.List;

public class ContainerBuilder extends UIRenderableBuilder<Container> {

    public UIDimension width;
    public UIDimension height;
    public Anchor anchorPoint;

    public Alignment.Horizontal horizontalAlignment;
    public Alignment.Vertical verticalAlignment;
    public Alignment.Direction alignmentDirection;
    public Overflow overflow;
    public Wrap wrap;

    public List<ContainerBuilder> childContainers;
    public List<ElementBuilder> elements;

    public ContainerBuilder() {

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

    public void dimensions(UIDimension width, UIDimension height) {
        this.width = width;
        this.height = height;
    }

    public void anchorPoint(Anchor anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public void horizontalAlignment(Alignment.Horizontal horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public void verticalAlignment(Alignment.Vertical verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public void alignmentDirection(Alignment.Direction alignmentDirection) {
        this.alignmentDirection = alignmentDirection;
    }

    public void overflow(Overflow overflow) {
        this.overflow = overflow;
    }

    public void wrap(Wrap wrap) {
        this.wrap = wrap;
    }

    public void addContainer(ContainerBuilder container) {
        this.childContainers.add(container);
    }

    public void addElement(ElementBuilder element) {
        this.elements.add(element);
    }
}
