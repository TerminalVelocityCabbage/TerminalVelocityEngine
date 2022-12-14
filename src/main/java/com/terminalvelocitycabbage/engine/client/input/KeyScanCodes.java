package com.terminalvelocitycabbage.engine.client.input;

public enum KeyScanCodes {

    ANY(1),
    NONE(0);

    private int scanCode;

    KeyScanCodes(int i) {
        this.scanCode = i;
    }

    public int getScanCodeInt() {
        return scanCode;
    }
}
