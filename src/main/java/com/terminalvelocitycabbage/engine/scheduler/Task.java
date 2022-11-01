package com.terminalvelocitycabbage.engine.scheduler;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;

import java.util.function.Consumer;

//TODO make this a record and create a class that has execute and init methods on it for the scheduler to run
public final class Task {

    private boolean initialized;
    private final Identifier identifier;
    private final Consumer<Task> consumer;
    private boolean remove;
    private final boolean repeat;
    private final long repeatInterval; //In millis
    private long lastExecuteTimeMillis;
    private boolean delay;
    private long delayInMilis;
    private long executeTime;

    public Task(Identifier identifier, Consumer<Task> consumer, boolean repeat, long repeatInterval, boolean delay, long delayInMillis) {
        this.identifier = identifier;
        this.consumer = consumer;
        this.remove = false;
        this.repeat = repeat;
        this.repeatInterval = repeatInterval;
        this.delay = delay;
        this.delayInMilis = delayInMillis;
    }

    //Used to set times for execute and such
    public Task init() {
        if (delay) executeTime = System.currentTimeMillis() + delayInMilis;
        if (repeat) lastExecuteTimeMillis = System.currentTimeMillis() - repeatInterval;
        initialized = true;
        return this;
    }

    public Identifier identifier() {
        return identifier;
    }

    public Consumer<Task> consumer() {
        return consumer;
    }

    public long lastExecuteTimeMillis() {
        return lastExecuteTimeMillis;
    }

    public void execute() {
        consumer().accept(this);
        lastExecuteTimeMillis = System.currentTimeMillis();
    }

    public boolean remove() {
        return remove;
    }

    public void markRemove() {
        remove = true;
    }

    public boolean repeat() {
        return repeat;
    }

    public long repeatInterval() {
        return repeatInterval;
    }

    public boolean delay() {
        return delay;
    }

    public long executeTime() {
        return executeTime;
    }

    public boolean initialized() {
        return initialized;
    }
}
