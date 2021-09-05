package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.client.renderer.ui.components.Margin;
import com.terminalvelocitycabbage.engine.client.renderer.ui.components.UIDimension;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class UnsafeUIRenderableBuilder<T extends UnsafeUIRenderableBuilder<T, K>, K extends UIRenderable> {

    protected final T self = (T) this;

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

    List<Consumer<K>> hoverConsumers;
    List<Consumer<K>> unHoverConsumers;
    List<Consumer<K>> leftClickConsumers;
    List<Consumer<K>> rightClickConsumers;
    List<DoubleClickRunnable> doubleClickConsumers;

    public UnsafeUIRenderableBuilder() {
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

    public T onHover(Consumer<K> consumer) {
        hoverConsumers.add(consumer);
        return this.self;
    }

    public T onUnHover(Consumer<K> consumer) {
        unHoverConsumers.add(consumer);
        return this.self;
    }

    public T onClick(Consumer<K> consumer) {
        leftClickConsumers.add(consumer);
        return this.self;
    }

    public T onRightClick(Consumer<K> consumer) {
        rightClickConsumers.add(consumer);
        return this.self;
    }

    public T onDoubleClick(int tickTime, Consumer<K> consumer) {
        doubleClickConsumers.add(new DoubleClickRunnable<>(tickTime, consumer));
        return this.self;
    }

    public T color(float r, float g, float b, float a) {
        this.backgroundRed.forceSetTarget(r);
        this.backgroundGreen.forceSetTarget(g);
        this.backgroundBlue.forceSetTarget(b);
        this.backgroundAlpha.forceSetTarget(a);
        return this.self;
    }

    public T borderColor(float r, float g, float b, float a) {
        this.borderRed.forceSetTarget(r);
        this.borderGreen.forceSetTarget(g);
        this.borderBlue.forceSetTarget(b);
        this.borderAlpha.forceSetTarget(a);
        return this.self;
    }

    public T borderRadius(int radius) {
        this.borderRadius.forceSetTarget(radius);
        return this.self;
    }

    public T borderThickness(int thickness) {
        this.borderThickness.forceSetTarget(thickness);
        return this.self;
    }

    public T margin(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setMargins(value, value, value, value);
        this.margin.setMarginUnits(unit, unit, unit, unit);
        return this.self;
    }

    public T margins(AnimatableUIValue left, AnimatableUIValue right, AnimatableUIValue top, AnimatableUIValue bottom) {
        this.margin.setMargins(left, right, top, bottom);
        return this.self;
    }

    public T marginUnits(UIDimension.Unit left, UIDimension.Unit right, UIDimension.Unit top, UIDimension.Unit bottom) {
        this.margin.setMarginUnits(left, right, top, bottom);
        return this.self;
    }

    public T marginLeft(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setLeft(value, unit);
        return this.self;
    }

    public T marginRight(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setRight(value, unit);
        return this.self;
    }

    public T marginTop(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setTop(value, unit);
        return this.self;
    }

    public T marginBottom(AnimatableUIValue value, UIDimension.Unit unit) {
        this.margin.setBottom(value, unit);
        return this.self;
    }

}
