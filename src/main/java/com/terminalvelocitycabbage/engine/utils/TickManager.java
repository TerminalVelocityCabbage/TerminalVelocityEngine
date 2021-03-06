package com.terminalvelocitycabbage.engine.utils;

public class TickManager {

    private final float msPerTick;

    private float compoundTime;
    private short ticks;

    public TickManager(float ticksPerSecond) {
        this.msPerTick = 1000F / ticksPerSecond;
        this.compoundTime = 0;
    }

    public void apply(float deltaTime) {
        this.compoundTime += deltaTime;
        while (this.compoundTime >= this.msPerTick) {
            this.ticks++;
            this.compoundTime -= this.msPerTick;
        }
    }

    public boolean hasTick() {
        if (this.ticks == 0) {
            return false;
        }
        this.ticks--;
        return true;
    }
}
