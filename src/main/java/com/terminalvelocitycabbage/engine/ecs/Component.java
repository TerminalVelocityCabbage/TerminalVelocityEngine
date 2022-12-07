package com.terminalvelocitycabbage.engine.ecs;

/**
 * A component is meant to be a way to store data, we intend for users to implement this on a {@link Record} with the
 * data required for that component.
 */
public interface Component {
    //Any data a user may want to store on that component.
    //This requires a 0 args constructor, so the component should have default values.
}
