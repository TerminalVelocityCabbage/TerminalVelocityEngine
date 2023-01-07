package com.terminalvelocitycabbage.engine.client.ui;

public abstract class GUI {

    public abstract void init();
    public abstract void draw(long vg);
    public abstract void update();
    public abstract void cleanup();
}
