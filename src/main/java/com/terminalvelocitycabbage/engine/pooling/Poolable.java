package com.terminalvelocitycabbage.engine.pooling;

/**
 * An interface that defines an object which can be pooled
 */
public interface Poolable {
    //Resets the current pooled object to its default values
    public void reset();
}
