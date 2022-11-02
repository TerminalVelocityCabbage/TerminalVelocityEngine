package com.terminalvelocitycabbage.engine.scheduler;

public class TaskContext {

    Task task;
    Object previousReturnValue;

    public TaskContext(Task task) {
        this.task = task;
    }

    public TaskContext(Task task, Object previousReturnValue) {
        this.task = task;
        this.previousReturnValue = previousReturnValue;
    }

    public Task task() {
        return task;
    }

    public Object previous() {
        return previousReturnValue;
    }
}
