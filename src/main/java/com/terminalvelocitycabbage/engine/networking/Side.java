package com.terminalvelocitycabbage.engine.networking;

public enum Side {

    CLIENT("client"),
    SERVER("server");

    final String side;

    Side(String side) {
        this.side = side;
    }
}
