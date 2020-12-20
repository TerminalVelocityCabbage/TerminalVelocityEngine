package com.terminalvelocitycabbage.engine.utils;

public class TickManager {

    private final float msPerTick;

    private float compoundTime;
    private long ticks;

    public TickManager(float ticksPerSecond) {
        this.msPerTick = 1000F / ticksPerSecond;
    }

    public void apply(float deltaTime) {
        this.compoundTime += deltaTime;
        while (this.compoundTime >= this.msPerTick) {
            this.ticks++;
            this.compoundTime -= this.msPerTick;
        }
    }

    public boolean hasTick() {
        if(this.ticks == 0) {
            return false;
        }
        this.ticks--;
        return true;
    }
}
