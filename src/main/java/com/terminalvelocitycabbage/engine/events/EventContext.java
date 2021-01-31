package com.terminalvelocitycabbage.engine.events;

public enum EventContext {

    CLIENT("client"),
    SERVER("server"),
    ENGINE("engine");

    String name;

    EventContext(String type) {
        this.name = type;
    }
}
