package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Margin;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UIRenderableBuilder<T extends UIRenderable> {

    AnimatableUIValue backgroundRed;
    AnimatableUIValue backgroundGreen;
    AnimatableUIValue backgroundBlue;
    AnimatableUIValue backgroundAlpha;
    AnimatableUIValue borderRed;
    AnimatableUIValue borderGreen;
    AnimatableUIValue borderBlue;
    AnimatableUIValue borderAlpha;
    AnimatableUIValue borderRadius;
    AnimatableUIValue borderThickness;
    Margin margin;

    List<Consumer<T>> hoverConsumers;
    List<Consumer<T>> unHoverConsumers;
    List<Consumer<T>> leftClickConsumers;
    List<Consumer<T>> rightClickConsumers;
    List<DoubleClickRunnable> doubleClickConsumers;

    public UIRenderableBuilder() {
        backgroundRed = new AnimatableUIValue(1);
        backgroundGreen = new AnimatableUIValue(1);
        backgroundBlue = new AnimatableUIValue(1);
        backgroundAlpha = new AnimatableUIValue(1);
        borderRed = new AnimatableUIValue(1);
        borderGreen = new AnimatableUIValue(1);
        borderBlue = new AnimatableUIValue(1);
        borderAlpha = new AnimatableUIValue(0);
        borderRadius = new AnimatableUIValue(0);
        borderThickness = new AnimatableUIValue(0);
        margin = new Margin();

        hoverConsumers = new ArrayList<>();
        unHoverConsumers = new ArrayList<>();
        leftClickConsumers = new ArrayList<>();
        rightClickConsumers = new ArrayList<>();
        doubleClickConsumers = new ArrayList<>();
    }

    public UIRenderableBuilder<T> onHover(Consumer<T> consumer) {
        hoverConsumers.add(consumer);
        return this;
    }

    public UIRenderableBuilder<T> onUnHover(Consumer<T> consumer) {
        unHoverConsumers.add(consumer);
        return this;
    }

    public UIRenderableBuilder<T> onClick(Consumer<T> consumer) {
        leftClickConsumers.add(consumer);
        return this;
    }

    public UIRenderableBuilder<T> onRightClick(Consumer<T> consumer) {
        rightClickConsumers.add(consumer);
        return this;
    }

    public UIRenderableBuilder<T> onDoubleClick(int tickTime, Consumer<T> consumer) {
        doubleClickConsumers.add(new DoubleClickRunnable(tickTime, consumer));
        return this;
    }

    public UIRenderableBuilder<T> color(float r, float g, float b, float a) {
        this.backgroundRed.setTarget(r);
        this.backgroundGreen.setTarget(g);
        this.backgroundBlue.setTarget(b);
        this.backgroundAlpha.setTarget(a);
        return this;
    }

    public UIRenderableBuilder<T> borderColor(float r, float g, float b, float a) {
        this.borderRed.setTarget(r);
        this.borderGreen.setTarget(g);
        this.borderBlue.setTarget(b);
        this.borderAlpha.setTarget(a);
        return this;
    }

    public UIRenderableBuilder<T> borderRadius(int radius) {
        this.borderRadius.setTarget(radius);
        return this;
    }

    public UIRenderableBuilder<T> borderThickness(int thickness) {
        this.borderThickness.setTarget(thickness);
        return this;
    }

    public UIRenderableBuilder<T> margin(AnimatableUIValue value, UIDimension.Unit unit) {
        return margins(value, value, value, value).marginUnits(unit, unit, unit, unit);
    }

    public UIRenderableBuilder<T> margins(AnimatableUIValue left, AnimatableUIValue right, AnimatableUIValue top, AnimatableUIValue bottom) {
        this.margin.setMargins(left, right, top, bottom);
        return this;
    }

    public UIRenderableBuilder<T> marginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
        this.margin.setMarginUnits(left, right, top, bottom);
        return this;
    }

    public UIRenderableBuilder<T> marginLeft(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setLeft(value, unit);
        return this;
    }

    public UIRenderableBuilder<T> marginRight(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setRight(value, unit);
        return this;
    }

    public UIRenderableBuilder<T> marginTop(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setTop(value, unit);
        return this;
    }

    public UIRenderableBuilder<T> marginBottom(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setBottom(value, unit);
        return this;
    }

}
