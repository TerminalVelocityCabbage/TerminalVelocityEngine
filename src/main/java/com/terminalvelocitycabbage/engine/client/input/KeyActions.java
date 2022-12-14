package com.terminalvelocitycabbage.engine.client.input;

public enum KeyActions {

    PRESS(0),
    RELEASE(1);

    private int action;

    KeyActions(int i) {
        this.action = i;
    }

    public int getActionInt() {
        return action;
    }
}
