package com.terminalvelocitycabbage.engine.client.input;

public enum KeyModifiers {

    NONE(0),
    SHIFT(1),
    CONTROL(2),
    ALT(4),
    SUPER(8),
    CAPS_LOCK(16),
    NUM_LOCK(32);

    private byte modifierId;

    KeyModifiers(byte modifierId) {
        this.modifierId = modifierId;
    }

    KeyModifiers(int i) {
        this((byte)i);
    }

    public byte getModifierByte() {
        return modifierId;
    }
}
