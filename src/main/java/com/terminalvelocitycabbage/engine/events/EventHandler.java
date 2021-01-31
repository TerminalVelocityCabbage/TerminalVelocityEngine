package com.terminalvelocitycabbage.engine.events;

import java.util.ArrayList;

public class EventHandler {

    private final EventContext context;
    private final ArrayList<Object> listeners = new ArrayList<>();

    public EventHandler(EventContext context) {
        this.context = context;
    }

    public EventContext getContext() {
        return context;
    }

    public void addListener(Object listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(Object listener) {
        listeners.remove(listener);
    }

    public ArrayList<Object> getListeners() {
        return listeners;
    }
}
