package com.terminalvelocitycabbage.engine.client.renderer.ui;

import com.terminalvelocitycabbage.engine.utils.EasingUtil;

public class AnimatableUIValue {

    float baseValue;
    float targetValue;
    float progress;
    float delay;
    float duration;
    EasingUtil.Function function;
    EasingUtil.Direction direction;

    public AnimatableUIValue(float defaultValue) {
        this.baseValue = defaultValue;
        this.targetValue = this.baseValue;
        this.progress = 1f;
        this.delay = 0f;
        this.duration = 0f;
        this.function = EasingUtil.Function.LINEAR;
        this.direction = EasingUtil.Direction.IN_OUT;
    }

    public AnimatableUIValue forceSetTarget(float target) {
        this.progress = 1f;
        this.baseValue = target;
        this.targetValue = target;
        return this;
    }

    public AnimatableUIValue setTarget(float target) {
        this.progress = 0f;
        this.targetValue = target;
        return this;
    }

    public AnimatableUIValue delay(float delay) {
        this.delay = delay;
        return this;
    }

    public AnimatableUIValue duration(float duration) {
        this.duration = duration;
        return this;
    }

    public AnimatableUIValue transition(EasingUtil.Function function, EasingUtil.Direction direction) {
        this.function = function;
        this.direction = direction;
        return this;
    }

    public float getValue() {
        return baseValue + (EasingUtil.ease(direction, function, progress) * (targetValue - baseValue));
    }

    public void unsetTarget() {
        this.setTarget(baseValue);
    }
}
