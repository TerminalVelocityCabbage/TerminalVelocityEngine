package com.terminalvelocitycabbage.engine.scheduler;

import com.terminalvelocitycabbage.engine.client.resources.Identifier;

import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.function.Consumer;

//TODO make this a record and create a class that has execute and init methods on it for the scheduler to run
public final class Task {

    private final StampedLock lock = new StampedLock();
    private boolean initialized;
    private final Identifier identifier;
    private final Consumer<TaskContext> consumer;
    private volatile boolean remove;
    private final boolean repeat;
    private final long repeatInterval; //In millis
    private long lastExecuteTimeMillis;
    private boolean delay;
    private long delayInMilis;
    private long executeTime;
    private final boolean async;
    private volatile boolean running;
    private TaskContext context;
    private final List<Task> subsequentTasks;

    public Task(Identifier identifier, Consumer<TaskContext> consumer, boolean repeat, long repeatInterval, boolean delay, long delayInMillis, boolean async, List<Task> subsequentTasks) {
        this.identifier = identifier;
        this.consumer = consumer;
        this.remove = false;
        this.repeat = repeat;
        this.repeatInterval = repeatInterval;
        this.delay = delay;
        this.delayInMilis = delayInMillis;
        this.async = async;
        this.running = false;
        this.context = new TaskContext(this);
        this.subsequentTasks = subsequentTasks;
    }

    //Used to set times for execute and such
    public Task init() {
        if (delay) executeTime = System.currentTimeMillis() + delayInMilis;
        if (repeat) lastExecuteTimeMillis = System.currentTimeMillis() - repeatInterval;
        initialized = true;
        return this;
    }

    //Used to set times for execute and such with a previous return value in context
    public Task init(Object previousReturn) {
        this.context.previousReturnValue = previousReturn;
        return init();
    }

    public Identifier identifier() {
        return identifier;
    }

    public Consumer<TaskContext> consumer() {
        return consumer;
    }

    public Consumer<TaskContext> getAndMarkConsumerRunning() {
        long l = lock.writeLock();
        markRunning();
        lock.unlockWrite(l);
        return consumer;
    }

    public long lastExecuteTimeMillis() {
        return lastExecuteTimeMillis;
    }

    public void execute() {
        consumer().accept(this.context());
        lastExecuteTimeMillis = System.currentTimeMillis();
    }

    public boolean remove() {
        return remove;
    }

    public void markRemove() {
        long l = lock.writeLock();
        remove = true;
        running = false;
        lock.unlockWrite(l);
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

    public boolean async() {
        return async;
    }

    public void markRunning() {
        this.running = true;
    }

    public boolean running() {
        return running;
    }

    public TaskContext context() {
        return this.context;
    }

    public boolean hasSubsequentTasks() {
        return subsequentTasks.size() > 0;
    }

    public List<Task> subsequentTasks() {
        return subsequentTasks;
    }

    public StampedLock getLock() {
        return lock;
    }
}
