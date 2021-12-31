package com.terminalvelocitycabbage.engine.client.renderer.ui;

import java.util.function.Consumer;

class DoubleClickRunnable<T extends UIRenderable> {

    int tickTime;
    Consumer<T> consumer;

    public DoubleClickRunnable(int tickTime, Consumer<T> consumer) {
        this.tickTime = tickTime;
        this.consumer = consumer;
    }

    public boolean shouldAccept(int time) {
        return tickTime >= time && time > 0;
    }
}
