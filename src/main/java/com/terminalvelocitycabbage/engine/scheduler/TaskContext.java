package com.terminalvelocitycabbage.engine.scheduler;

import java.util.Optional;

public class TaskContext {

    Task task;
    Object previousReturnValue;
    Optional<Object> value;

    public TaskContext(Task task) {
        this.task = task;
        this.value = Optional.empty();
    }

    public TaskContext(Task task, Object previousReturnValue) {
        this.task = task;
        this.previousReturnValue = previousReturnValue;
    }

    public Task task() {
        return task;
    }

    public Optional<Object> previous() {
        return Optional.of(previousReturnValue);
    }

    public Optional<Object> value() {
        return value;
    }

    public void setReturn(Object value) {
        this.value = Optional.of(value);
    }
}
