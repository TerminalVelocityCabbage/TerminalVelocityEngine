package com.terminalvelocitycabbage.engine.ecs;

/**
 * A system that operates on any family of components.
 */
public abstract class System {

    //The priority of this system, lower numbers are executed first
    private int priority;
    //Whether this system is currently processing
    private boolean processing;

    /**
     * Creates a system with the given priority. 0 is the highest priority
     * @param priority the priority or order that this system takes in the pool of systems
     */
    public System(int priority) {
        this.priority = priority;
    }

    /**
     * @param deltaTime the amount of time in milliseconds passed since the last update
     */
    abstract void update(float deltaTime);

    /**
     * @return the priority of this system
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority an int to replace this systems current priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return a boolean representing whether this system is currently being processed
     */
    public boolean isProcessing() {
        return processing;
    }

    /**
     * @param processing a boolean to represent the new current state of this system
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
